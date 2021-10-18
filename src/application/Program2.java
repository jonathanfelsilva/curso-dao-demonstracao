package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.dao.impl.DepartmentDaoJDBC;
import model.entities.Department;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Program2 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("===TESTE 1 - FindById===");
        Department department = departmentDao.findById(2);
        System.out.println(department);

        System.out.println("===TESTE 2 - FindAll==");
        List<Department> departmentList = departmentDao.findAll();
        departmentList.forEach(System.out::println);

        System.out.println("===TESTE 3 - Insert===");
        Department departmentInclusao = new Department(null, "Cubo mágico");
        departmentDao.insert(departmentInclusao);
        System.out.println(departmentInclusao.getId());

        System.out.println("===TESTE 4 - Update===");
        Department departmentEdicao = new Department(1, "Ediçãaaaaaaa ID 1");
        departmentDao.update(departmentEdicao);

        System.out.println("===TESTE 5 - Delete===");
        System.out.println("Informe o ID que deseja remover: ");
        int idParaRemover = sc.nextInt();
        sc.nextLine();
        departmentDao.deleteById(idParaRemover);
    }
}
