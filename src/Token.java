
public class Token {
	
	public Type type;
	public String content;
	
	public Token(Type t, String content){
		this.type = t;
		this.content = content;
	}
	
	public String toString(){
		if(type == Type.ATOM || type == Type.VAR || type == Type.NUMBER || type == Type.COMMENT){
			return type+"<"+content+">";
		}
		return type.toString();
	}
}
