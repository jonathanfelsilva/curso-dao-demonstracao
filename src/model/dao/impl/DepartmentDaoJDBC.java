package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection connection;

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("Id"));
        dep.setName(rs.getString("Name"));
        return dep;
    }

    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("INSERT INTO department "
                    + "(Name) "
                    + "VALUES "
                    + "(?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, department.getName());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()){
                    int id = generatedKeys.getInt(1);
                    department.setId(id);
                } else {
                    throw new DbException("Nenhuma linha foi afetada. Erro inesperado.");
                }

                DB.closeResultSet(generatedKeys);
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }

    }

    @Override
    public void update(Department department) {

        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("UPDATE department "
                    + "set name = ? "
                    + "where id = ?");

            statement.setString(1, department.getName());
            statement.setInt(2, department.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }

    }

    @Override
    public void deleteById(Integer id) {

        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("DELETE from department "
                    + "where id = ?");

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }

    }

    @Override
    public Department findById(Integer id) {

        PreparedStatement statement = null;
        ResultSet departmentFound = null;

        try {
            statement = connection.prepareStatement("Select * from department "
                    + "where id = ?");

            statement.setInt(1, id);
            departmentFound = statement.executeQuery();

            if (departmentFound.next()){
                Department department = instantiateDepartment(departmentFound);
                return department;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(departmentFound);
        }
    }

    @Override
    public List<Department> findAll() {

        PreparedStatement statement = null;
        ResultSet departmentFound = null;

        List<Department> departmentsFound = new ArrayList<>();

        try {
            statement = connection.prepareStatement("Select * from department "
            + "order by name");

            departmentFound = statement.executeQuery();

            if (departmentFound.next()){
                Department department = instantiateDepartment(departmentFound);
                departmentsFound.add(department);

                while ((departmentFound.next())){
                    department = instantiateDepartment(departmentFound);
                    departmentsFound.add(department);
                }

                return departmentsFound;

            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(departmentFound);
        }
    }
}
