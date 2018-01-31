<#macro pager pageNo pageSize recordCount pageCount toURL method>  
  <#-- 定义局部变量pageCount保存总页数 -->
  <#if recordCount==0><#return/></#if>
<#-- 输出分页样式 -->
<#-- 页号越界处理 -->
  <#if (pageNo > pageCount)>
    <#assign pageNo=pageCount>
  </#if>
  <#if (pageNo < 1)>
    <#assign pageNo=1>
  </#if>
<div class="col-md-4 col-sm-12">
</div>
<div class="col-md-8 col-sm-12">
<#-- 输出分页表单 -->
<form method="${method}" action="" name="qPagerForm">
<#-- 把请求中的所有参数当作隐藏表单域(无法解决一个参数对应多个值的情况) -->
<div class="dataTables_paginate paging_bootstrap">
    <ul class="pagination">
<#list RequestParameters?keys as key>
<#if (key!="page" && RequestParameters[key]??)>
    <li><input type="hidden" name="${key}" value="${RequestParameters[key]}"/></li>
</#if>
</#list>
    <li><input type="hidden" name="page" value="${pageNo}"/>
<span>共${recordCount}条记录，当前第<b>${pageNo}/${pageCount}</b>页</span></li>
<#-- 上一页处理 -->
  <#if (pageNo == 1)>
      <li class="prev disabled"><a href="#" title="Previous"><i
              class="icon-angle-left"></i></a></li>
  <#else>
      <li>
          <#--<a href="javascript:void(0)" onclick="turnOverPage(${pageNo - 1})">&laquo;&nbsp;上一页</a>-->
          <a href="javascript:void(0)" title="Previous" onclick="turnOverPage(${pageNo - 1})"><i
                  class="icon-angle-left"></i></a>
      </li>
  </#if>
<#-- 如果前面页数过多,显示... -->
    <#assign start=1>
    <#if (pageNo > 4)>
    <#assign start=(pageNo - 1)>
<li><a href="javascript:void(0)" onclick="turnOverPage(1)">1</a>
<li><a href="javascript:void(0)" onclick="turnOverPage(2)">2</a></li>
<li><a href="javascript:void(0)" style="cursor:default">&hellip;</a></li>
    </#if>
<#-- 显示当前页号和它附近的页号 -->
    <#assign end=(pageNo + 1)>
    <#if (end > pageCount)>
        <#assign end=pageCount>
    </#if>
  <#list start..end as i>
    <#if (pageNo==i)>
<li class="active"><span >${i}</span></li>
        <#else>
            <li><a href="javascript:void(0)" onclick="turnOverPage(${i})">${i}</a>    </li>
    </#if>
  </#list>
<#-- 如果后面页数过多,显示... -->
  <#if (end < pageCount - 2)>
<li><a href="javascript:void(0)" style="cursor:default">&hellip;</a></li> 
  </#if>
  <#if (end < pageCount - 1)>
      <li><a href="javascript:void(0)" onclick="turnOverPage(${pageCount - 1})">${pageCount-1}</a></li>
  </#if>
  <#if (end < pageCount)>
      <li><a href="javascript:void(0)" onclick="turnOverPage(${pageCount})">${pageCount}</a></li>
  </#if>
<#-- 下一页处理 -->
  <#if (pageNo == pageCount)>
      <#--<li><span class="prev disabled">下一页&nbsp;&raquo;</span></li>-->
      <li class="next"><a href="#" title="Next"><i
              class="icon-angle-right"></i></a></li>
  <#else>
      <#--<li><a href="javascript:void(0)" onclick="turnOverPage(${pageNo + 1})">下一页&nbsp;&raquo;</a></li>-->
      <li class="next"><a href="javascript:void(0)" onclick="turnOverPage(${pageNo + 1})" title="Next"><i
              class="icon-angle-right"></i></a></li>
  </#if>
</ul>
</div>  
<script language="javascript">
  function turnOverPage(no){
    var qForm=document.qPagerForm;
    if(no>${pageCount}){no=${pageCount};}
    if(no<1){no=1;}
    qForm.page.value=no;
    qForm.action="${toURL}";
    qForm.submit();
  }
</script>
</div> 
</#macro>