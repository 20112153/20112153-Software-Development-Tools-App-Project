import java.nio.file.Path
import ui.GymConsole

fun main() {
    GymConsole(dataFile = Path.of("data", "gym-data.json")).run()
}
