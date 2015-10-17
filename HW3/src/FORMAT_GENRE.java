import java.io.IOException;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class FORMAT_GENRE extends EvalFunc<String> {
	private final String netId = " <lxc121830>";
	@Override
	public String exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0) {
			return null;
		}
		
		try {
			StringBuffer fmt = new StringBuffer();
		
			String genres = (String)input.get(0);
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
				//the last one, add <NetId>
				if (i == items.length-1) {
					fmt.append(netId);
				}
				else {
					fmt.append(",");
				}
			}
			return fmt.toString()  ;
		} catch(Exception e) {
			throw new IOException("Caught exception processing input row ", e);
		}
	}
}
