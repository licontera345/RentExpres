package test.com.pinguela.rentexpres.dao;

import com.pinguela.rentexpres.dao.impl.ProvinceDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.ProvinceDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class ProvinceDAOTest {

    public static void main(String[] args) {
        Connection connection = null;
        ProvinceDAOImpl dao = new ProvinceDAOImpl();

        try {
            connection = JDBCUtils.getConnection();
            System.out.println("Connection established successfully.");

            ProvinceDTO province = new ProvinceDTO();
            province.setProvinceName("Galicia");
            dao.create(connection, province);
            System.out.println("Province created successfully.");

            ProvinceDTO found = null;
            int lastId = -1;
            try (PreparedStatement ps = connection.prepareStatement("SELECT MAX(province_id) AS lastId FROM province")) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    lastId = rs.getInt("lastId");
                    found = dao.findById(connection, lastId);
                }
            }

            if (found != null) {
                System.out.println("Province found: " + found.getProvinceName() + " (id=" + lastId + ")");
            } else {
                System.out.println("No province found after creation.");
            }

            if (found != null) {
                found.setProvinceName("Galicia Atlantic");
                dao.update(connection, found);
                System.out.println("Province updated successfully.");
            }

            List<ProvinceDTO> all = dao.findAll(connection);
            System.out.println("Province list (" + all.size() + "):");
            for (ProvinceDTO dto : all) {
                System.out.println("  " + dto.getProvinceName());
            }

            if (lastId != -1) {
                dao.delete(connection, lastId);
                System.out.println("Province deleted successfully.");
            }

        } catch (DataException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(connection);
        }
    }
}