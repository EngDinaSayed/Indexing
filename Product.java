import java.io.IOException;
import java.io.RandomAccessFile;

public class Product {

  int  Product_ID;
  int  Product_Price;
  int  Product_Quantity;

  public Product()
  {

  }

  public Product(int id,int price, int quantity)
  {
      Product_ID=id;
      Product_Price=price;
      Product_Quantity=quantity;

  }

    public int getProduct_ID() {
        return Product_ID;
    }

    public int getProduct_Price() {
        return Product_Price;
    }

    public int getProduct_Quantity() {
        return Product_Quantity;
    }

    public void setProduct_ID(int product_ID) {
        Product_ID = product_ID;
    }

    public void setProduct_Price(int product_Price) {
        Product_Price = product_Price;
    }

    public void setProduct_Quantity(int product_Quantity) {
        Product_Quantity = product_Quantity;
    }

    public void readProduct(RandomAccessFile file, long offset) throws IOException {
        file.seek(offset);
        Product_ID = file.readInt();
       file.seek(offset+4);
        Product_Price = file.readInt();
        file.seek(offset+8);
        Product_Price = file.readInt();
    }
}
