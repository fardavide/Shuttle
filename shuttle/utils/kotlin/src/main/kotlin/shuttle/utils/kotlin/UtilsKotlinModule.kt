package shuttle.utils.kotlin

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DispatchersModule::class])
@ComponentScan
class UtilsKotlinModule

const val AppVersionQualifier = "App version"
