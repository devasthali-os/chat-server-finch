package com.chat.server

import com.typesafe.config.{Config, ConfigFactory}

object AppConfig {

  val environment = System.getProperty("environment", "dev")

  private val config: Config = ConfigFactory.load("application.conf").getConfig("app")
  val EnvConfig = config.getConfig(environment).withFallback(config.getConfig("default"))

  val AppName = EnvConfig.getString("name")
  val AppVersion = EnvConfig.getString("version")
  val AppEnvironment = EnvConfig.getString("env")
}
