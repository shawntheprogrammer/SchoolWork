import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

@Description(name = "format_genre",
	value = "_FUNC_(genres) - formate the input genres string "
			+ "like before: Children's|Adventure|Animation"
			+ "after: 1) Children's, 2) Adventure & 3) Animation <NetId> :hive",
	extended = "Example:\n"
			+ " > SELECT _FUNC_(genres_string) FROM src;\n")
/*
Before formatting: Children's
After formatting: 1) Children's <NetId> :hive
Before formatting: Animation|Children's
After formatting: 1) Children's & 2) Animation <NetId> :hive
Before formatting: Children's|Adventure|Animation
After formatting: 1) Children's, 2) Adventure & 3) Animation <NetId> :hive
<NetId> -- netid
 */
public class UDFFormateGenreMovies extends UDF{
	private final String netId = " <lxc121830> :hive";
	
	public String evaluate(String genres){
		StringBuffer fmt = new StringBuffer();
		
		String items[] = genres.split("\\|");
		for (int i = 0; i < items.length; i++) {
			if (i == items.length-1) {
				int l = fmt.lastIndexOf(",");
				if (l > 0) {
					fmt.deleteCharAt(l);
					fmt.append(" & ");
				}
			}
			fmt.append(Integer.toString(i+1));
			fmt.append(") ");
			fmt.append(items[i]);
			//the last one, add <NetId> :hive
			if (i == items.length-1) {
				fmt.append(netId);
			}
			else {
				fmt.append(",");
			}
		}
		
		return fmt.toString();
	}
}