package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.SQLOutput;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {

        SellerDao sellerDao = DaoFactory.createSellerDao();
        Seller seller = sellerDao.findById(6);

        Scanner sc = new Scanner(System.in);

        System.out.println("=== Teste 1: ===");
        System.out.println(seller);

        List<Seller> sellersById = sellerDao.findByDepartment(1);
        System.out.println("\n=== Teste 2: ===");
        sellersById.forEach(System.out::println);

        List<Seller> allSellers = sellerDao.findAll();
        System.out.println("\n=== Teste 3: ===");
        allSellers.forEach(System.out::println);

        System.out.println("\n=== Teste 4: ===");
        Seller newSeller = new Seller(null, "Jonathan Teste", "jonathan@jonathan", new Date(), 1000.00, new Department(2, "Teste"));
        sellerDao.insert(newSeller);
        System.out.println("Deu certo! Novo ID: " + newSeller.getId());

        System.out.println("\n=== Teste 5 ===");
        Seller newSellerForUpdate = sellerDao.findById(5);
        newSellerForUpdate.setName("Teste nome");
        sellerDao.update(newSellerForUpdate);
        System.out.println("Update completed.");

        System.out.println("\n=== Teste 6 ===");
        System.out.print("Enter ID to delete: ");
        int idToDelete = sc.nextInt();
        sc.nextLine();

        sellerDao.deleteById(idToDelete);
        System.out.println("Delete completed.");

    }
}
