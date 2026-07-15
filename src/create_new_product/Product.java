package create_new_product;

import java.time.LocalDate;

public class Product {

    private int id;
    private String name;
    private int categoryId;
    private LocalDate saleDate;
    private int quantity;
    private boolean isDelete;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", saleDate=" + saleDate +
                ", quantity=" + quantity +
                ", isDelete=" + isDelete +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public Product(int id,
                   String name,
                   int categoryId,
                   LocalDate saleDate,
                   int quantity,
                   boolean isDelete) {

        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.saleDate = saleDate;
        this.quantity = quantity;
        this.isDelete = isDelete;
    }
}