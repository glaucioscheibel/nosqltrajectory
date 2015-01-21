package br.udesc.mca.sec1.projeto.dao.relational;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import br.udesc.mca.sec1.projeto.model.Customer;
import br.udesc.mca.sec1.projeto.model.CustomerData;

public class PostgreSQLPersistence extends RelationalPersistence {
    private static PostgreSQLPersistence instance;

    private Connection db;

    public static PostgreSQLPersistence getInstance() {
        if (instance == null) {
            instance = new PostgreSQLPersistence();
        }
        return instance;
    }

    public PostgreSQLPersistence() {
        try {
            // TODO: Substituir por um pool de conexoes
            this.db = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/" + DBNAME, "postgres", "senha");
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public Customer store(Customer c) {
        this.log.info("store(" + c + ")");
        Customer aux = this.findById(c.getId());
        try {
            this.db.setAutoCommit(false);
            PreparedStatement ps = null;
            if (aux == null) {
                ps = this.db.prepareStatement("insert into customer(id, name) values(?, ?)");
                ps.setInt(1, c.getId());
                ps.setString(2, c.getName());
            } else {
                ps = this.db.prepareStatement("update customer set name=? where id=?");
                ps.setString(1, c.getName());
                ps.setInt(2, c.getId());
            }
            ps.executeUpdate();
            ps.close();
            ps = this.db.prepareStatement("delete from customerdata where id=?");
            ps.setInt(1, c.getId());
            ps.executeUpdate();
            List<CustomerData> lcd = c.getCustomerData();
            if (lcd != null && !lcd.isEmpty()) {
                ps = this.db.prepareStatement("insert into customerdata(id, key, value) values(?, ?, ?)");
                for (CustomerData cd : lcd) {
                    ps.clearParameters();
                    ps.setInt(1, c.getId());
                    ps.setString(2, cd.getDataKey());
                    ps.setString(3, cd.getDataValue());
                    ps.executeUpdate();
                }
            }
            this.db.commit();
        } catch (SQLException e) {
            try {
                this.db.rollback();
            } catch (SQLException e1) {}
            log.error(e.getMessage(), e);
        } finally {
            try {
                this.db.setAutoCommit(true);
            } catch (SQLException e) {}
        }
        return c;
    }

    @Override
    public List<Customer> findAll() {
        this.log.info("findAll()");
        List<Customer> lc = new LinkedList<>();
        try {
            Statement st = this.db.createStatement();
            ResultSet rs = st.executeQuery("SELECT customer.id, customer.name, customerdata.key, customerdata.value "
                    + "FROM customer LEFT JOIN customerdata ON customer.id = customerdata.id");
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getInt(1));
                c.setName(rs.getString(2));
                int i = lc.indexOf(c);
                if (i >= 0) {
                    c = lc.get(i);
                } else {
                    lc.add(c);
                }
                String key = rs.getString(3);
                if (!rs.wasNull()) {
                    c.addCustomerData(key, rs.getString(4));
                }
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return lc;
    }

    @Override
    public Customer findById(Integer id) {
        this.log.info("findById(" + id + ")");
        Customer c = null;
        try {
            PreparedStatement ps = this.db.prepareStatement("SELECT customer.id, customer.name, customerdata.key, "
                    + "customerdata.value FROM customer LEFT JOIN customerdata ON customer.id = customerdata.id where "
                    + "customer.id = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                c = new Customer();
                c.setId(rs.getInt(1));
                c.setName(rs.getString(2));
                String key = rs.getString(3);
                if (!rs.wasNull()) {
                    c.addCustomerData(key, rs.getString(4));
                }
            }
            while (rs.next()) {
                c.addCustomerData(rs.getString(3), rs.getString(4));
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return c;
    }

    @Override
    public void deleteById(Integer id) {
        this.log.info("deleteById(" + id + ")");
        try {
            this.db.setAutoCommit(false);
            PreparedStatement ps = this.db.prepareStatement("delete from customer where id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps = this.db.prepareStatement("delete from customerdata where id=?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            this.db.commit();
        } catch (SQLException e) {
            try {
                this.db.rollback();
            } catch (SQLException e1) {}
            log.error(e.getMessage(), e);
        } finally {
            try {
                this.db.setAutoCommit(true);
            } catch (SQLException e) {}
        }
    }

    public void deleteAll() {
        try {
            Statement st = this.db.createStatement();
            st.executeUpdate("truncate table customer");
            st.executeUpdate("truncate table customerdata");
            st.close();
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        if (this.db != null) {
            try {
                this.db.close();
            } catch (SQLException e) {}
            this.db = null;
        }
    }
}
