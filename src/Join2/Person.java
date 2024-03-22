package Join2;
import java.util.StringTokenizer;

public class Person {
   public String table_name;
   protected String person_name;

   Person(String code) {
      StringTokenizer itr = new StringTokenizer(code.toString());
      this.table_name = itr.nextToken();
      this.person_name = itr.nextToken();
   }

   public Boolean isProfessor() {
      return this.table_name.compareTo("P") == 0;
   }
}
