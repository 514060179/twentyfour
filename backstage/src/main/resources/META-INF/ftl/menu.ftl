<div class="page-sidebar navbar-collapse collapse">
    <!-- BEGIN SIDEBAR MENU -->
    <ul class="page-sidebar-menu">
        <li>
            <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
            <div class="sidebar-toggler hidden-phone"></div>
            <!-- BEGIN SIDEBAR TOGGLER BUTTON -->
        </li>
        <li>
            <!-- BEGIN RESPONSIVE QUICK SEARCH FORM -->
            <form class="sidebar-search" action="extra_search.html" method="POST">
                <div class="form-container">
                    <div class="input-box">
                        <a href="javascript:;" class="remove"></a>
                        <input type="text" placeholder="Search..."/>
                        <input type="button" class="submit" value=" "/>
                    </div>
                </div>
            </form>
            <!-- END RESPONSIVE QUICK SEARCH FORM -->
        </li>
        <li class="start ">
            <a href="index.html">
                <i class="icon-home"></i>
                <span class="title">首页</span>
            </a>
        </li>
<#if menuList??>
    <#list menuList as item>
        <li class="<#if item.lable='${lable!}'>active</#if>">
            <a href="${base!}/${item.url!}">
                <i class="${item.icon!}"></i>
                <span class="title">${item.name!}</span>
                <span class="selected"></span>
            </a>
            <#if item.menuList??>
                <#list item.menuList as item2>
                <ul class="sub-menu">
                    <li class="">
                        <a href="${item2.url!}">
                            <span class="badge badge-roundless badge-important"></span>${item2.name!}</a>
                    </li>
                </ul>
                </#list>
            </#if>
        </li>
    </#list>
</#if>




    </ul>
    <!-- END SIDEBAR MENU -->
</div>