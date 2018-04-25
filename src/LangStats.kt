import com.eclipsesource.json.*
import java.io.*

class Submission(
    val id: String,
    val team: String,
    val problem: String,
    val language: String,
    val time: String
)

fun main(args: Array<String>) {
    val submissions = ArrayList<Submission>()
    File("data/event-feed.json").forEachLine { line ->
        val value = Json.parse(line).asObject()!!
        if (value.getString("type", "") != "submissions") return@forEachLine
        val data = value.get("data").asObject()!!
        submissions += Submission(
            id = data.getString("id", ""),
            team = data.getString("team_id", ""),
            problem = data.getString("problem_id", ""),
            language = data.getString("language_id", ""),
            time = data.getString("contest_time", "")
        )
    }
    printStats(" -= TOTAL =- ", submissions)
    submissions.map { it.language }.distinct().sorted().forEach { lang ->
        printStats("LANG " + lang.padStart(8), submissions.filter { it.language == lang })
    }
}

private fun printStats(header: String, subs: List<Submission>) {
    val count = subs.size.toString().padStart(4)
    val teams = subs.distinctBy { it.team }.size.toString().padStart(3)
    val minTime = subs.minBy { it.time }!!.time
    val maxTime = subs.maxBy { it.time }!!.time
    println("$header: $count submissions from $teams teams ($minTime -- $maxTime)")
}