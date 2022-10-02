package name.denyago.volumes

import com.lordcodes.turtle.ShellRunException
import com.lordcodes.turtle.ShellScript

class MontedVolumes(private val shell: ShellScript) {

    fun statuses(mountPoints: List<MountPoint>): List<DriveState> {
        val allMounts = shell.command("mount")
        return mountPoints.map { mountPoint ->
            DriveState(mountPoint, allMounts.contains(mountPoint.toString()))
        }
    }

    fun mount(mountPoint: MountPoint): Result<MountPoint> {
        try {
            shell.command("ls", listOf("/foo"))
        } catch (e: ShellRunException) {
            return Result.failure(e)
        }
        return Result.success(mountPoint)
    }
}
