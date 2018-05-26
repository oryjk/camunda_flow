package com.betalpha.fosun.condition

import com.betalpha.fosun.api.process.Condition
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
    new Condition("${result==0}", "不通过"),
    new Condition("${result==1}", "通过"),
    new Condition("${result==0 && 1 == 1}", "否"),
    new Condition("${result==1 && 1 == 1}", "是"),
    new Condition("${result==2}", "继续研究"),
    new Condition("${result==3}", "全票通过"),
    //    new Condition("${result==3}", "高收益"),
    //    new Condition("${result==5}", "部分同意"),
    //    new Condition("${result==4}", "高等级")
  )


  override def getConditions(): java.util.List[Condition] = {
    conditions.asJava
  }
}

