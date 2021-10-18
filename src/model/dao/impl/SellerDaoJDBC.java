package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("INSERT INTO seller " +
                    "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                    "VALUES " +
                    "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, seller.getName());
            statement.setString(2, seller.getEmail());
            statement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            statement.setDouble(4, seller.getBaseSalary());
            statement.setInt(5, seller.getDepartment().getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0 ){
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()){
                    int id = generatedKeys.getInt(1);
                    seller.setId(id);
                }
                DB.closeResultSet(generatedKeys);
            } else {
                throw new DbException("Nenhuma linha foi afetada. Erro inesperado.");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(Seller seller) {

    }

    @Override
    public Seller findById(Integer id) {


        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement("select *, dp.name as DepName from seller se "
                    + "left join department dp on dp.Id = se.DepartmentId "
                    + "where se.Id = ?");

            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Department dep = instantiateDepartment(resultSet);
                Seller seller = instantiateSeller(resultSet, dep);
                return seller;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }

    private Seller instantiateSeller(ResultSet resultSet, Department dep) throws SQLException {
        Seller seller = new Seller();

        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setDepartment(dep);

        return seller;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {

        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement("select *, dp.name as DepName from seller se "
                    + "left join department dp on dp.Id = se.DepartmentId ");

            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                Map<Integer, Department> departmentsMap = new HashMap<>();
                List<Seller> sellers = new ArrayList<>();

                Department dep = instantiateDepartment(resultSet);
                sellers.add(instantiateSeller(resultSet, dep));

                departmentsMap.put(resultSet.getInt("DepartmentId"), dep);

                while (resultSet.next()) {
                    dep = departmentsMap.get(resultSet.getInt("DepartmentId"));

                    if (dep == null) {
                        dep = instantiateDepartment(resultSet);
                        departmentsMap.put(resultSet.getInt("DepartmentId"), dep);
                    }

                    sellers.add(instantiateSeller(resultSet, dep));
                }

                return sellers;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } catch (NullPointerException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Seller> findByDepartment(Integer departmentId) {
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.prepareStatement("select *, dp.name as DepName from seller se "
                    + "left join department dp on dp.Id = se.DepartmentId "
                    + "where se.DepartmentId = ?"
                    + " order by se.Name");

            statement.setInt(1, departmentId);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Department dep = instantiateDepartment(resultSet);
                List<Seller> sellers = new ArrayList<>();
                sellers.add(instantiateSeller(resultSet, dep));

                while (resultSet.next()) {
                    sellers.add(instantiateSeller(resultSet, dep));
                }

                return sellers;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } catch (NullPointerException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(resultSet);
        }

    }
}
