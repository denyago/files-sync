package name.denyago.pages

data class HddState(val busy: Boolean)
data class DriveState(val mountPoint: String, val connected: Boolean)
data class IndexPage(val hddState: HddState, val driveStates: List<DriveState>, val lastDriveError: String)
