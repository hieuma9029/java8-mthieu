import java.util.Date;

public class Bai9 {

    private int id;
    private String name;
    private int categoryId;
    private Date saleDate;
    private int quantity;
    private boolean isDelete;

    public Bai9(int id, String name, int categoryId, Date saleDate, int quantity, boolean isDelete) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.saleDate = saleDate;
        this.quantity = quantity;
        this.isDelete = isDelete;
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

    public Date getSaleDate() {
        return saleDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isDelete() {
        return isDelete;
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

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

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
}