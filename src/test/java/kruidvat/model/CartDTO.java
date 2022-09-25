package kruidvat.model;

public class CartDTO {

    private ProductDTO product;
    private int qty;


    public ProductDTO getProduct() {
        return product;
    }

    public CartDTO setProduct(ProductDTO product) {
        this.product = product;
        return this;
    }

    public int getQty() {
        return qty;
    }

    public CartDTO setQty(int qty) {
        this.qty = qty;
        return this;
    }

}
