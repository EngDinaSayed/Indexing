import java.io.File;
import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class main {
    private static final int record = 12;  //defines the size of each record
   // private static final long rec=8;
    public static void createIndexFile() throws FileNotFoundException, IOException{

        RandomAccessFile file;
        Index i = new Index();
        //int key; //the id of each record as primary key for index file
        //long offset; //the position of the record
        ArrayList<Index>index = new ArrayList();
        try{
            file = new RandomAccessFile("FromTA.IbtehalYahiaMg90ifv59K.bin", "rw");
            long FileSize = file.length();
            file.seek(0);
            long numOfRecords = FileSize / record; //calculate the number of records

            for(int j = 0; j<numOfRecords; j++){ //seek till end of file

                file.seek(j*record); //seek at the begining of each record
                i.offset = file.getFilePointer(); //get the posistion of current record and store it in
               i.key = file.readInt(); //read the id and store it in the key
                //System.out.println(i.key);
                //System.out.println(i.offset);
                index.add(i);
                i=new Index();

            }
            Collections.sort(index, new Comparator<Index>() {
                @Override
                public int compare(Index o1, Index o2) {
                    if(o1.key == o2.key)
                    return 0;
                 return o1.key < o2.key ? -1 : 1;
                }
            });
            System.out.println("Contents of index: " + index);

           /* RandomAccessFile indexFile = new RandomAccessFile("index.txt","rw");
            for(int x=0; x<index.size(); x++)
            {
                indexFile.seek(0);
                indexFile.writeInt(x);
            }*/
            File f = new File("indexfile.bin");
           RandomAccessFile indexFile = new RandomAccessFile(f, "rw");
           // indexFile.seek(0);
            for(int q = 0; q<index.size(); q++){
                indexFile.seek(f.length());
                indexFile.write(q);
            }
           /* indexFile.seek(0);
            int y;
            y=indexFile.readInt();
            System.out.println(y);*/
        }
        catch(IOException e){
            e.getStackTrace();
        }
    }


    public static long SearchById(int key,RandomAccessFile file) throws IOException {

        file=new RandomAccessFile("indexfile.bin","rw");


            long FileSize=file.length();
            long numOfRecords=FileSize/(record-1);


        long low = 0;
        long high =  numOfRecords;
        Index mid = new Index();
        long off=file.readLong();
        long pos=mid.key * record;

        while (low <= high){
            mid.key = (int)(low + high)/2;
            file.seek(pos);
            if(mid.key == key){
                file.seek(pos+4);
                return off;

            }
            else if(mid.key>key){
                high = mid.key-1;
            }
            else if(mid.key<key){
                low = mid.key+1;
            }
        }
        file.close();
        return off;

    }

    public static void write_Index( Index newIndex, RandomAccessFile indexfile) throws FileNotFoundException, IOException {

        indexfile.seek(0);

        long no_records=indexfile.length()/record;

        Index currentRec = new Index();
        Index temp;

        while (indexfile.length()!=no_records)
        {
            for (int i = 0; i < indexfile.length()+1; i++)
            {
                long pos=i*record;
                    indexfile.seek(pos);
                    currentRec.key = indexfile.readInt();
                    indexfile.seek(pos+4);
                    currentRec.offset = indexfile.readLong();
                    if (newIndex.key < currentRec.key) {
                        temp = currentRec;
                        currentRec = newIndex;
                        newIndex = temp;
                        indexfile.seek(pos);
                        indexfile.writeInt(currentRec.key);
                        indexfile.seek(pos+4);
                         indexfile.writeLong(currentRec.offset);
                    }
                //indexfile.seek(pos);
                }
            }
        }




   public static void Insert_Product () throws IOException {

       RandomAccessFile datafile = new RandomAccessFile("FromTA.IbtehalYahiaMg90ifv59K.bin","rw");
       RandomAccessFile indexfile = new RandomAccessFile("indexfile.bin","rw");

        int key;
        int Price;
        int Quantity;
        Scanner s = new Scanner(System.in);

       System.out.println ("Insert the new product key: ");
       key = s.nextInt();
       long offset =SearchById(key,indexfile);
        if(offset==-1)
        {
            System.out.println("this product is already exist");
        }
        else {
            System.out.println("Enter the price: ");
            Price= s.nextInt();
            System.out.println("Enter the quantity: ");
            Quantity= s.nextInt();
            Product products = new Product(key,Price,Quantity);
            long off=datafile.length();
            datafile.seek(datafile.length());
            datafile.writeInt(key);
            datafile.writeInt(Price);
            datafile.writeInt(Quantity);

            Index index=new Index(key,off);
            write_Index(index, indexfile);

        }
        datafile.close();
        indexfile.close();

       /* for(int i = 0; i < NumOfRecords; i ++)
        {

            ID = s.nextInt();


            else
            {
            Product products = new Product();
            products[i] = new Product(ID,Price,Quantity);
            newIndexes[i].key = ID;
            newIndexes[i].offset= datafile.length();

            datafile.seek(datafile.length());
            datafile.writeInt(ID);
            datafile.writeInt(Price);
            datafile.writeInt(Quantity);
            write_Index(newIndexes, indexfile);

            }
            int offset =SearchById(ID,indexfile);

        }*/

    }

    public  static void find_Product() throws IOException {

        RandomAccessFile datafile = new RandomAccessFile ("FromTA.IbtehalYahiaMg90ifv59K.bin", "rw");
        RandomAccessFile indexfile = new RandomAccessFile("indexfile.bin", "rw");

        Scanner s = new Scanner(System.in);

        System.out.println("Insert the ID of the product you want to search for");
        int ID = s.nextInt();

        long offset = SearchById( ID,indexfile);

        if(offset == -1)
        {
            System.out.println("ID is not found");
        }


        else
        {
            Product targetproduct = new Product();
            targetproduct.readProduct(datafile, offset);

            System.out.print(targetproduct.getProduct_ID()+"    "+ targetproduct.getProduct_Price()+"    "+ targetproduct.getProduct_Quantity());
        }



    }
     public static void Delete() throws IOException
     {
         RandomAccessFile datafile = new RandomAccessFile ("FromTA.IbtehalYahiaMg90ifv59K.bin", "rw");
         RandomAccessFile indexfile = new RandomAccessFile("indexfile.bin", "rw");

         int key;
         Scanner s = new Scanner(System.in);

         System.out.println ("Insert the id of the product you want to delete ");
         key = s.nextInt();

         long offset=SearchById(key,indexfile);
         long no_records=indexfile.length()/record;

         if(offset==-1)
         {
             System.out.println("this ID is not found");
         }
         else
         {
             char x='*';
             datafile.seek(offset);
             datafile.write(x);
             //delete record from index file, then shift records
             while(indexfile.length()!=no_records)
             {
                for (int i = 0; i < indexfile.length(); i++)
                 {
                     Index nextRec = new Index();
                     Index currentRec = new Index();
                     Index temp = new Index();
                     long pos=i*record;
                     indexfile.seek(pos);
                     currentRec.key = indexfile.readInt();
                     indexfile.seek(pos+4);
                     currentRec.offset = indexfile.readInt();
                     if (key == currentRec.key)
                     {
                         temp = currentRec;
                         for(int j=i+1; j<indexfile.length(); j++){
                             indexfile.seek(record*j);
                             nextRec.key = indexfile.readInt();
                             indexfile.seek(record*j +4);
                             nextRec.offset = indexfile.readInt();
                             currentRec = nextRec;
                             indexfile.seek(pos);
                             indexfile.writeInt(currentRec.key);
                             indexfile.writeLong(currentRec.offset);
                         }

                         indexfile.seek(indexfile.length());
                         indexfile.writeInt(temp.key);
                         indexfile.writeLong(temp.offset);
                     }

                 }
                 indexfile.seek(indexfile.length());
                 indexfile.setLength(indexfile.length()-8);
             }
             }

         }




   public static void update_Product() throws IOException {

        RandomAccessFile datafile = new RandomAccessFile ("FromTA.IbtehalYahiaMg90ifv59K.bin", "rw");
        RandomAccessFile indexfile = new RandomAccessFile("indexfile.bin", "rw");

        Scanner s = new Scanner(System.in);
       // Index targetRecord = null;
        long pos;
        int price;
        int quantity;

        System.out.println("Write the ID of the product you want to update its data: ");
        int ID = s.nextInt();

        long offset = SearchById(ID,indexfile);

        if(offset == -1){
            System.out.println("ID is not found");
        }
        else{
            pos=offset;
         //   pos = targetRecord.offset;
            datafile.seek(pos+4);
            System.out.println("Please enter new price: ");
            price = s.nextInt();
            datafile.writeInt(price);
            datafile.seek(pos+8);
            System.out.println("Please enter new quantity: ");
            quantity = s.nextInt();
            datafile.writeInt(quantity);

        }

    }









    public static void main(String[] args) throws IOException {
      createIndexFile();
      System.out.println("Please select an operation: ");
      System.out.println("1. Insert: ");
      System.out.println("2. Delete: ");
      System.out.println("3. Update: ");
      System.out.println("4. Search: ");
      System.out.println("5. Exit: ");
      int choice;
      boolean correct = true;
      Scanner s = new Scanner(System.in);
      choice = s.nextInt();
      while(correct == true){
          if(choice == 1){

              Insert_Product();


          }
          else if(choice == 2){
              Delete();


          }
          else if(choice == 3){
              update_Product();

          }
          else if(choice == 4){
              find_Product();

          }
          else if(choice == 5){
              correct = false;
          }
          else{
              System.out.println("Please select an operation!");
          }
      }

    }

}
