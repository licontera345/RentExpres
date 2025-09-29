package com.pinguela.rentexpres.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.dao.UsuarioDAO;
import com.pinguela.rentexpres.dao.impl.UsuarioDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.exception.RentexpresException;
import com.pinguela.rentexpres.model.Results;
import com.pinguela.rentexpres.model.UsuarioCriteria;
import com.pinguela.rentexpres.model.UsuarioDTO;
import com.pinguela.rentexpres.service.FileService;
import com.pinguela.rentexpres.service.UsuarioService;
import com.pinguela.rentexpres.util.JDBCUtils;

/**
 * Implementación de {@link UsuarioService} que gestiona la lógica de negocio
 * para operaciones CRUD y autenticación de usuarios, delegando las consultas
 * a {@link UsuarioDAO} y controlando transacciones JDBC.
 *
 * @author Jesús Félix
 * @version 1.0
 */
public class UsuarioServiceImpl implements UsuarioService {

    /** Logger para mensajes de información y errores. */
    private static final Logger logger = LogManager.getLogger(UsuarioServiceImpl.class);
    private static final String USER_IMAGE_FOLDER = "usuarios";

    /** DAO usado para acceder a datos de usuario. */
    private UsuarioDAO usuarioDAO;
    private final FileService fileService;

    /**
     * Constructor por defecto. Crea una instancia de {@link UsuarioDAOImpl} como
     * implementación de {@link UsuarioDAO}.
     */
    public UsuarioServiceImpl() {
        this.usuarioDAO = new UsuarioDAOImpl();
        this.fileService = new FileServiceImpl();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Obtiene un usuario por su ID. Abre una conexión, inicia transacción,
     * delega en {@link UsuarioDAO#findById(Connection, Integer)}, limpia la
     * contraseña en el DTO antes de devolverlo y cierra transacción.
     * </p>
     *
     * @param id Identificador del usuario a buscar.
     * @return {@link UsuarioDTO} sin la contraseña, o null si no existe.
     * @throws RentexpresException Si ocurre un error de conexión o datos.
     */
    @Override
    public UsuarioDTO findById(Integer id) throws RentexpresException {
        Connection connection = null;
        UsuarioDTO usuario = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);

            usuario = usuarioDAO.findById(connection, id);
            if (usuario != null) {
                usuario.setContrasena(null);
            }

            JDBCUtils.commitTransaction(connection);
            logger.info("findById de Usuario completado. ID: {}", id);
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en findById de Usuario: ", e);
            throw new RentexpresException("Error en findById de Usuario", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return usuario;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Recupera todos los usuarios. Abre una conexión, inicia transacción,
     * llama a {@link UsuarioDAO#findAll(Connection)}, limpia contraseñas, cierra
     * transacción y devuelve la lista.
     * </p>
     *
     * @return Lista de {@link UsuarioDTO} sin contraseñas, o lista vacía si no hay
     *         usuarios.
     * @throws RentexpresException Si ocurre un error de conexión o datos.
     */
    @Override
    public List<UsuarioDTO> findAll() throws RentexpresException {
        Connection connection = null;
        List<UsuarioDTO> lista = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);

            lista = usuarioDAO.findAll(connection);
            if (lista != null) {
                for (UsuarioDTO u : lista) {
                    u.setContrasena(null);
                }
            }

            JDBCUtils.commitTransaction(connection);
            logger.info("findAll de Usuario completado. Cantidad: {}", (lista != null ? lista.size() : 0));
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en findAll de Usuario: ", e);
            throw new RentexpresException("Error en findAll de Usuario", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return lista;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Crea un nuevo usuario. Abre conexión, inicia transacción, llama a
     * {@link UsuarioDAO#create(Connection, UsuarioDTO)}, envía correo de
     * bienvenida si se crea correctamente, sube imágenes asociadas, limpia la
     * contraseña antes de devolver resultado y maneja transacción.
     * </p>
     *
     * @param usuario DTO con datos del nuevo usuario (incluyendo imágenes). El
     *                campo contraseña debe estar en texto claro.
     * @return true si la creación y operaciones asociadas tuvieron éxito; false si
     *         la creación devolvió false.
     * @throws RentexpresException Si hay error en conexión, datos o envío de correo.
     */
    @Override
    public boolean create(UsuarioDTO usuario) throws RentexpresException {
        Connection connection = null;
        boolean creado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);

            creado = usuarioDAO.create(connection, usuario);
            if (creado) {
                JDBCUtils.commitTransaction(connection);
                logger.info("Usuario creado exitosamente. ID: {}", usuario.getId());

                // Enviar correo de bienvenida
                MailServiceImpl mailService = new MailServiceImpl();
                String asunto = "Bienvenido a RentExpress";
                String mensaje = "Estimado " + usuario.getNombre() + ", bienvenido a RentExpress.";
                mailService.enviar(usuario.getEmail(), asunto, mensaje);

                // Limpiar contraseña antes de devolver
                usuario.setContrasena(null);

                storeUserImages(usuario, true);
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn("No se pudo crear el Usuario.");
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en create de Usuario: ", e);
            throw new RentexpresException("Error en create de Usuario", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return creado;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Actualiza un usuario existente. Abre conexión, inicia transacción, llama a
     * {@link UsuarioDAO#update(Connection, UsuarioDTO)}, limpia contraseña y,
     * si hay imágenes, las sube. Maneja transacción en caso de éxito o fallo.
     * </p>
     *
     * @param usuario DTO con datos actualizados del usuario (ID no nulo). El campo
     *                contraseña debe estar en texto claro.
     * @return true si la actualización tuvo éxito; false si el DAO devolvió false.
     * @throws RentexpresException Si hay error en conexión o datos.
     */
    @Override
    public boolean update(UsuarioDTO usuario) throws RentexpresException {
        Connection connection = null;
        boolean actualizado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);

            actualizado = usuarioDAO.update(connection, usuario);
            if (actualizado) {
                JDBCUtils.commitTransaction(connection);
                logger.info("Usuario actualizado exitosamente. ID: {}", usuario.getId());

                usuario.setContrasena(null);
                storeUserImages(usuario, true);
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn("No se pudo actualizar el Usuario. ID: {}", usuario.getId());
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en update de Usuario: ", e);
            throw new RentexpresException("Error en update de Usuario", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return actualizado;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Elimina un usuario existente. Abre conexión, inicia transacción, llama a
     * {@link UsuarioDAO#delete(Connection, UsuarioDTO, Integer)}, y maneja
     * transacción en función del resultado.
     * </p>
     *
     * @param usuario DTO del usuario a eliminar (ID no nulo).
     * @param id      Identificador del usuario a eliminar (debe coincidir con
     *                usuario.getId()).
     * @return true si la eliminación tuvo éxito; false si el DAO devolvió false.
     * @throws RentexpresException Si hay error en conexión o datos.
     */
    @Override
    public boolean delete(UsuarioDTO usuario, Integer id) throws RentexpresException {
        Connection connection = null;
        boolean eliminado = false;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);

            eliminado = usuarioDAO.delete(connection, usuario, id);
            if (eliminado) {
                JDBCUtils.commitTransaction(connection);
                logger.info("Usuario eliminado exitosamente. ID: {}", id);
                deleteUserImages(id);
            } else {
                JDBCUtils.rollbackTransaction(connection);
                logger.warn("No se pudo eliminar el Usuario. ID: {}", id);
            }
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en delete de Usuario: ", e);
            throw new RentexpresException("Error en delete de Usuario", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return eliminado;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Autentica un usuario. Abre conexión, inicia transacción, llama a
     * {@link UsuarioDAO#autenticar(Connection, String, String)}, limpia la
     * contraseña si la autenticación es exitosa y cierra la transacción.
     * </p>
     *
     * @param nombreUsuario     Nombre de usuario para autenticar.
     * @param contrasenaEnClaro Contraseña en texto claro.
     * @return {@link UsuarioDTO} sin contraseña si la autenticación fue
     *         exitosa; null en caso contrario.
     * @throws RentexpresException Si hay error en conexión o datos.
     */
    @Override
    public UsuarioDTO autenticar(String nombreUsuario, String contrasenaEnClaro) throws RentexpresException {
        Connection connection = null;
        UsuarioDTO usuario = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);

            usuario = usuarioDAO.autenticar(connection, nombreUsuario, contrasenaEnClaro);
            if (usuario != null) {
                usuario.setContrasena(null);
            }

            JDBCUtils.commitTransaction(connection);
            logger.info("Autenticación de Usuario completada. Usuario: {}", nombreUsuario);
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en autenticación de Usuario: ", e);
            throw new RentexpresException("Error en autenticación de Usuario", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return usuario;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Busca usuarios según criterios de paginación y filtros. Abre conexión,
     * inicia transacción, llama a
     * {@link UsuarioDAO#findByCriteria(Connection, UsuarioCriteria)}, limpia
     * contraseñas, cierra transacción y devuelve resultados.
     * </p>
     *
     * @param criteria Objeto {@link UsuarioCriteria} que contiene filtros y
     *                 parámetros de paginación.
     * @return {@link Results}&lt;{@link UsuarioDTO}&gt; con la lista paginada de
     *         usuarios, sin contraseñas, y el total de registros.
     * @throws RentexpresException Si hay error en conexión o datos.
     */
    @Override
    public Results<UsuarioDTO> findByCriteria(UsuarioCriteria criteria) throws RentexpresException {
        Connection connection = null;
        Results<UsuarioDTO> results = null;
        try {
            connection = JDBCUtils.getConnection();
            JDBCUtils.beginTransaction(connection);

            results = usuarioDAO.findByCriteria(connection, criteria);
            if (results != null && results.getResults() != null) {
                for (UsuarioDTO u : results.getResults()) {
                    u.setContrasena(null);
                }
            }

            JDBCUtils.commitTransaction(connection);
            logger.info("findByCriteria de Usuario completado. Página {} (Tamaño: {}), Total registros: {}",
                    criteria.getPageNumber(), criteria.getPageSize(),
                    results != null ? results.getTotalRecords() : 0);
        } catch (SQLException | DataException e) {
            JDBCUtils.rollbackTransaction(connection);
            logger.error("Error en findByCriteria de Usuario: ", e);
            throw new RentexpresException("Error en findByCriteria de Usuario", e);
        } finally {
            JDBCUtils.close(connection);
        }
        return results;
    }

    /**
     * Guarda las imágenes asociadas a un usuario en el sistema de archivos. Si se
     * indica, elimina previamente las imágenes existentes del usuario.
     *
     * @param usuario          usuario al que pertenecen las imágenes
     * @param replaceExisting  si es {@code true} elimina las imágenes existentes
     *                         antes de guardar las nuevas
     */
    private void storeUserImages(UsuarioDTO usuario, boolean replaceExisting) {
        if (usuario == null || usuario.getId() == null || usuario.getImagenes() == null
                || usuario.getImagenes().isEmpty()) {
            return;
        }

        Long userId = usuario.getId().longValue();
        if (replaceExisting) {
            deleteUserImages(usuario.getId());
        }

        for (File image : usuario.getImagenes()) {
            if (image == null) {
                continue;
            }
            try (InputStream inputStream = new FileInputStream(image)) {
                String storedPath = fileService.saveFile(inputStream, image.getName(), USER_IMAGE_FOLDER, userId);
                if (storedPath == null) {
                    logger.warn("No se pudo guardar la imagen {} para el usuario {}", image.getName(), userId);
                }
            } catch (IOException e) {
                logger.error("Error al guardar la imagen {} del usuario {}", image.getName(), userId, e);
            }
        }
    }

    /**
     * Elimina todas las imágenes asociadas a un usuario concreto del sistema de
     * archivos.
     *
     * @param userId identificador del usuario
     */
    private void deleteUserImages(Integer userId) {
        if (userId == null) {
            return;
        }
        List<String> existingImages = fileService.listFiles(USER_IMAGE_FOLDER, userId.longValue());
        for (String imagePath : existingImages) {
            if (!fileService.deleteFile(imagePath)) {
                logger.warn("No se pudo eliminar la imagen {} del usuario {}", imagePath, userId);
            }
        }
    }
}
