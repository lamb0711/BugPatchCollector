package edu.handong.csee.isel.Runner;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import edu.handong.csee.isel.githubcommitparser.GithubPatchCollector;
import edu.handong.csee.isel.patch.LocalGitRepositoryPatchCollector;
import edu.handong.csee.isel.patch.MyExecutor;

/**
 * -i, URL or URI(github.com, reference file having github URLs, Local
 * Repository) -o, directory of result file. [-r], reference relative to bug
 * commit. [-m], minimum printing of lines. [-x], maximum printing of lines.
 * 
 * If is there '-r', check that commit message have the pattern by reference to
 * '-r' option value. Else, check that commit message have the 'bug' or 'fix'
 * keyword.
 * 
 * @author imseongbin
 */
public class Main {
	String gitRepositoryPath = null;
	String githubURL = null;
	String listOfGithubURLFile = null;
	String resultDirectory = null;
	String reference = null;
	String label = null;
	int conditionMax = 0;
	int conditionMin = 0;
	boolean isThread;
	boolean help;

	public static void main(String[] args) {
		Main bc = new Main();
		bc.run(args);
	}

	public void run(String[] args) {
		Options options = createOptions();

		if (parseOptions(options, args)) {
			if (help) {
				printHelp(options);
				return;
			}
			
			long start = System.currentTimeMillis();
			System.out.println("start: "+start/1000.0); //will be removed
			
			if (gitRepositoryPath != null) {
				LocalGitRepositoryPatchCollector gr = new LocalGitRepositoryPatchCollector(gitRepositoryPath,
						resultDirectory, reference, conditionMax, conditionMin, isThread);
				gr.run();

			} else if (githubURL != null || listOfGithubURLFile != null) {
				GithubPatchCollector gh = new GithubPatchCollector(githubURL, resultDirectory, listOfGithubURLFile,
						String.valueOf(conditionMax), String.valueOf(conditionMin), label, isThread);
				gh.run();
			}
			long end = System.currentTimeMillis();
			System.out.println( "실행 시간 : " + ( end - start )/1000.0 );
//			for(String author : MyExecutor.authors) {
//				System.out.println(author);
//			}
//			System.out.println("총 기여한 사람의 수: "+MyExecutor.authors.size());

		}
	}

	private boolean parseOptions(Options options, String[] args) {
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);

			String input = cmd.getOptionValue("i");

			try {
				if (input.contains("github.com")) {
					githubURL = input;
				} else {
					File file = new File(input);
					if (file.exists()) {
						if (file.isDirectory()) {
							gitRepositoryPath = input;
						} else {
							listOfGithubURLFile = input;
						}
					} else {
						throw new Exception("input file not exist!");
					}
				}

				if (cmd.hasOption("x") || cmd.hasOption("m")) {
					if (cmd.hasOption("x") && cmd.hasOption("m")) {
						conditionMax = Integer.parseInt(cmd.getOptionValue("x"));
						conditionMin = Integer.parseInt(cmd.getOptionValue("m"));
						if (conditionMin > conditionMax) {
							throw new Exception("Max must be bigger than min!");
						}

					} else {
						throw new Exception("'x' and 'm' Option must be together!");
					}

				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				printHelp(options);
				return false;
			}

			label = cmd.getOptionValue("l");
			reference = cmd.getOptionValue("r");
			resultDirectory = cmd.getOptionValue("o");
			help = cmd.hasOption("h");
			isThread = cmd.hasOption("t");

		} catch (Exception e) {
			printHelp(options);
			return false;
		}

		return true;
	}

	private Options createOptions() {
		Options options = new Options();

		options.addOption(Option.builder("i").longOpt("input").desc(
				"Three input type: URL or URI(github.com, reference file having github URLs, Local " + "Repository)")
				.hasArg().argName("URI or URL").required().build());

		options.addOption(Option.builder("o").longOpt("result").desc("directory will have result file").hasArg()
				.argName("directory").required().build());

		options.addOption(Option.builder("r").longOpt("reference")
				.desc("If you have list of bug commit IDs, make a file to have the list, and push the file").hasArg()
				.argName("reference relative to bug").build());

		options.addOption(Option.builder("x").longOpt("max")
				.desc("Set a Max lines of each result patch. Only count '+++' and '---' lines. must used with '-m'")
				.hasArg().argName("Max lines of patch").build());

		options.addOption(Option.builder("m").longOpt("min")
				.desc("Set a Min lines of each result patch. This Option need to be used with 'M' Option(MaxLine).")
				.hasArg().argName("Min lines of patch").build());

		options.addOption(Option.builder("l").longOpt("label").desc("Set a bug label of github").hasArg()
				.argName("Find coincident commit with label").build());

		options.addOption(Option.builder("t").longOpt("thread")
				.desc("Using threads in your cpu, you can speed up. Only do well if input is local repository.")
				.build());

		options.addOption(Option.builder("h").longOpt("help").desc("Help").build());

		return options;
	}

	private void printHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		String header = "Collecting bug-patch program";
		String footer = "\nPlease report issues at https://github.com/HGUISEL/BugPatchCollector/issues";
		formatter.printHelp("BugPatchCollector", header, options, footer, true);
	}

}
