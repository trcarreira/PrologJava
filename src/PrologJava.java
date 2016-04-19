import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author tuliocarreira
 */
public class PrologJava {

	public static String getIdentifier(String s, int lexemeBegin) {
		int lookahead = lexemeBegin;
		while (lookahead < s.length()) {
			if (Character.isUpperCase(s.charAt(lookahead)) || Character.isLowerCase(s.charAt(lookahead))
					|| Character.isDigit(s.charAt(lookahead)) || s.charAt(lookahead) == '_') {
				lookahead++;
			} else
				return s.substring(lexemeBegin, lookahead);
		}
		return s.substring(lexemeBegin, lookahead);
	}

	public static String getNum(String s, int lexemeBegin) {
		int lookahead = lexemeBegin;
		while (lookahead < s.length()) {
			if (Character.isDigit(s.charAt(lookahead))) {
				lookahead++;
			} else
				return s.substring(lexemeBegin, lookahead);
		}
		return s.substring(lexemeBegin, lookahead);
	}

	public static String getComment(String s, int lexemeBegin) {
		int lookahead = lexemeBegin;
		while (lookahead < s.length() && s.charAt(lookahead) != '*' && s.charAt(lookahead + 1) != '\\') {
			lookahead++;
		}

		return s.substring(lexemeBegin, lookahead);
	}

	public static List<Token> analisaEntrada(String input) {
		List<Token> result = new ArrayList<Token>();
		char c;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			switch (c) {
			case ',':
				result.add(new Token(Type.VIRGULA, ","));
				break;
			case '"':
				result.add(new Token(Type.ASPA, "\""));
				break;
			case '(':
				result.add(new Token(Type.PARENTESE_ESQ, "("));
				break;
			case ')':
				result.add(new Token(Type.PARENTESE_DIR, ")"));
				break;
			case '[':
				result.add(new Token(Type.COLCHETE_ESQ, "["));
				break;
			case ']':
				result.add(new Token(Type.COLCHETE_DIR, "]"));
				break;
			case '.':
				result.add(new Token(Type.PONTO, "."));
				break;
			case ' ':
				break;
			case '\n':
				break;
			default:
				if (Character.isDigit(c)) {
					String num = getNum(input, i);
					i += num.length() - 1;
					result.add(new Token(Type.NUMBER, num));
				} else if (Character.isUpperCase(c) || c == '_') {
					String var = getIdentifier(input, i);
					i += var.length() - 1;
					result.add(new Token(Type.VAR, var));
				} else if (Character.isLowerCase(c)) {
					String atom = getIdentifier(input, i);
					i += atom.length() - 1;
					result.add(new Token(Type.ATOM, atom));
				} else if (input.charAt(i) == ':' && input.charAt(i + 1) == '-') {
					i += 1;
					result.add(new Token(Type.IMPLICA, ":-"));
				} else if (input.charAt(i) == '/' && input.charAt(i + 1) == '*') {
					String comment = getComment(input, i + 2);
					i += comment.length() + 3;
					result.add(new Token(Type.COMMENT, comment));
				} else {
					System.out.println("Caracter não permitido em tokens: " + c);
				}
			}
		}
		return result;

	}

	static String readFile(String path) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded);
	}

	public static void main(String[] args) {
		String input = "";
		try {
			input = readFile("./programa.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		input = input.replace("\r\n", " ");
		List<Token> tokens = analisaEntrada(input);
		for (Token t : tokens) {
			if (t.type == Type.PONTO || t.type == Type.COMMENT) {
				System.out.println(t + " ");
			} else
				System.out.print(t + " ");
		}
	}
}
