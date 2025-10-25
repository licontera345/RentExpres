
package test.com.pinguela.rentexpres.dao;

import java.sql.Connection;
import java.util.List;

import com.pinguela.rentexpres.dao.VehicleStatusDAO;
import com.pinguela.rentexpres.dao.impl.VehicleStatusDAOImpl;
import com.pinguela.rentexpres.exception.DataException;
import com.pinguela.rentexpres.model.VehicleStatusDTO;
import com.pinguela.rentexpres.util.JDBCUtils;

public class VehicleStatusDAOTest {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            VehicleStatusDAO dao = new VehicleStatusDAOImpl();

            VehicleStatusDTO status = dao.findById(connection, 1, "en");
            System.out.println("Vehicle status found: " + status);

            List<VehicleStatusDTO> list = dao.findAll(connection, "en");
            System.out.println("Total vehicle statuses: " + list.size());
            for (VehicleStatusDTO dto : list) {
                System.out.println(dto);
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
