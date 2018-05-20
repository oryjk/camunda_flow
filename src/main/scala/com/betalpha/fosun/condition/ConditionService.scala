package com.betalpha.fosun.condition

import org.springframework.stereotype.Service

import scala.collection.JavaConverters._

/**
  * Created by WangRui on 17/05/2018.
  */
trait ConditionService {


  def getConditions(): java.util.List[Condition]

}


@Service
class DefaultConditionService extends ConditionService {
  private val conditions = List(
    Condition("${result==0}", "不通过"),
    Condition("${result==1}", "通过"),
    Condition("${result==2}", "继续研究"),
    Condition("${result==3}", "高收益"),
    Condition("${result==4}", "高等级"))


  override def getConditions(): java.util.List[Condition] = {
    conditions.asJava
  }
}

case class Condition(condition: String, name: String)
