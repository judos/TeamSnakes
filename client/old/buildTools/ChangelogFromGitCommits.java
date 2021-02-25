import java.util.HashMap;

/**
 * @since 03.09.2015
 * @author Julian Schelker
 */
public class ChangelogFromGitCommits {

	public static void main(String[] args) {
		HashMap<String, String> argumentsByCommand = convertArgs(args);

		if (argumentsByCommand.containsKey("changelogToReleaseNotes"))
			changelogToReleaseNotes(argumentsByCommand);
	}

	private static void changelogToReleaseNotes(HashMap<String, String> argumentsByCommand) {
		// String lastCommitInPrevious
	}

	private static HashMap<String, String> convertArgs(String[] args) {
		HashMap<String, String> result = new HashMap<String, String>();
		for (int i = 0; i < args.length; i++) {
			String s = args[i];
			// remove prefix -
			if (s.startsWith("-"))
				s = s.substring(1);
			// split command and value by first space
			String[] commandValue = s.split(" ", 2);
			if (commandValue.length == 2)
				result.put(commandValue[0], commandValue[1]);
			else if (commandValue.length == 1)
				result.put(commandValue[0], "");
		}
		return null;
	}

}
