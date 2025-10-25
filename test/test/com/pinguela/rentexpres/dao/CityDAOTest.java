package test.com.pinguela.rentexpres.dao;

import com.pinguela.rentexpres.dao.impl.CityDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.CityDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class CityDAOTest {

    public static void main(String[] args) {
        Connection connection = null;
        CityDAOImpl dao = new CityDAOImpl();

        try {
            connection = JDBCUtils.getConnection();
            System.out.println("Connection established successfully.");

            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT IGNORE INTO province (province_id, province_name) VALUES (1, 'Madrid')")) {
                ps.executeUpdate();
            }

            CityDTO city = new CityDTO();
            city.setCityName("Madrid Center");
            city.setProvinceId(1);
            dao.create(connection, city);
            System.out.println("City created successfully.");

            CityDTO found = null;
            try (PreparedStatement ps = connection.prepareStatement("SELECT MAX(city_id) AS lastId FROM city")) {
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int lastId = rs.getInt("lastId");
                    found = dao.findById(connection, lastId);
                }
            }

            if (found != null) {
                System.out.println("City found: " + found.getCityName() +
                        " (province_id=" + found.getProvinceId() + ")");
            }

            if (found != null) {
                found.setCityName("Madrid Capital");
                dao.update(connection, found);
                System.out.println("City updated successfully.");
            }

            List<CityDTO> all = dao.findAll(connection);
            System.out.println("City list (" + all.size() + "):");
            for (CityDTO dto : all) {
                System.out.println("  " + dto.getCityName() + " (province_id=" + dto.getProvinceId() + ")");
            }

            dao.delete(connection, found);
            System.out.println("City deleted successfully.");

        } catch (DataException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(connection);
        }
    }
}