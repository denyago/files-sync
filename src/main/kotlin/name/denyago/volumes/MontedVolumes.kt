package name.denyago.volumes

import com.lordcodes.turtle.ShellScript

class MontedVolumes(private val shell: ShellScript) {

    fun statuses(mountPoints: List<String>): List<DriveState> {
        val allMounts = shell.command("mount")
        return mountPoints.map { mountPoint ->
            DriveState(mountPoint, allMounts.contains(mountPoint))
        }
    }
}
