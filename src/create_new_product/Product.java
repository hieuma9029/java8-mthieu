package create_new_product;

import java.time.LocalDate;

public class Product {

    private int id;
    private String name;
    private int categoryId;
    private LocalDate saleDate;
    private int qulity;
    private boolean isDelete;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryId=" + categoryId +
                ", saleDate=" + saleDate +
                ", quantity=" + qulity +
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

    public void setQulity(int quantity) {
        this.qulity = quantity;
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

    public int getQulity() {
        return qulity;
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
        this.qulity = quantity;
        this.isDelete = isDelete;
    }
}