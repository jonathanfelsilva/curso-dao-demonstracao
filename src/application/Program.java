package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {

    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(6);

        System.out.println("=== Teste 1: ===");
        System.out.println(seller);

        List<Seller> sellersById = sellerDao.findByDepartment(2);
        System.out.println("=== Teste 2: ===");
        sellersById.forEach(System.out::println);

    }
}
