package IA;

public class Regle {
	private String[] premisses;
	private String[] actions;
	
	
	public static void main(String[] args) {
		System.out.println(new Regle("mode(Destruction),coupReussi,!bateau(coule)=>continuerDirection"));
	}
	
	public Regle(String data) {		
		String[] regleDecomposee = data.split("=>");
		premisses = regleDecomposee[0].split(",");
		actions = regleDecomposee[1].split(",");
	}
	
	/*public boolean satisfaitCondition(faits) {
		
	}
	
	public boolean satisfaitConditions(faits) {
		
	}*/

	@Override
	public String toString() {
		String regleString = "";
		for(String s : premisses)
			regleString+=s;
		for(String s : actions)
			regleString+=s;
		return regleString;
	}
}
