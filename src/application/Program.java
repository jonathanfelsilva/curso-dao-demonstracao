package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Program {

    public static void main(String[] args) {


        Department department = new Department(1, "Teste");

        SellerDao sellerDao = DaoFactory.createSellerDao();

        Seller seller = sellerDao.findById(5);

        System.out.println(department);
        System.out.println(seller);

    }
}
