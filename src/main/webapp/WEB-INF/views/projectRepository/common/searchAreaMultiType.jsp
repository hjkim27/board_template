<%--
  Created by IntelliJ IDEA.
  User: aa827
  Date: 2024-09-08
  Time: 오전 12:33
  To change this template use File | Settings | File Templates.
  다중검색(filterType) 페이지 분리
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<link rel="stylesheet" type="text/css" href="${contextPath}/static/css/custom/project.css">
<div id="loadTarget">
    <%-- ----------- --%>
    <input type="hidden" id="filterType" name="filterType" value="${search.filterType}">
    <input type="hidden" id="sortColumn" name="sortColumn" value="${search.sortColumn}">
    <input type="hidden" id="desc" name="desc" value="${search.desc}">
    <%-- ----------- --%>
    <input type="text" id="searchValue" name="searchValue" class="min-size" value="${search.searchValue}">
    <%-- ----------- --%>
    <!-- Button trigger modal -->
    <button id="filterType-btn" type="button" class="btn bg-white-hover-blue br-dark-blue min-size" data-toggle="modal"
            data-target="#filterTypeModal">
        Type
    </button>

    <!-- Modal -->
    <div id="filterTypeModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="filterTypeModalLabel">
        <div class="modal-dialog" role="document">
            <div id="filterTypeModalContent" class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Select Type</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <c:forEach var="item" items="${labelList}">
                        <div name="filterType" class="modal-search-btn-multi grid-gap-10"
                             style="grid-template-columns: minmax(0px, 20px) auto" data-value="${item.labelId}">
                            <div class="grid-gap-10" style="grid-template-columns: 20px auto">
                                <div style="width:12px; height: 12px; background-color: ${item.color}"></div>
                                <div>${item.name}</div>
                                <div style="grid-column: 2; font-size: 12px">${item.description}</div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
    <%-- ----------- --%>
    <!-- Button trigger modal -->
    <button id="sortColumn-btn" type="button" class="btn bg-white-hover-blue br-dark-blue min-size" data-toggle="modal"
            data-target="#sortColumnModal">
        Sort
    </button>
    <!-- Modal -->
    <div id="sortColumnModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="sortColumnModalLabel">
        <div class="modal-dialog" role="document">
            <div id="sortColumnModalContent" class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Select Order</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <div name="sortColumn" class="modal-search-btn" data-value="8" data-desc="true">
                        <i class="fas fa-check selectChecksortColumn"></i>
                        Newest
                    </div>
                    <div name="sortColumn" class="modal-search-btn" data-value="8">
                        <div>Oldest</div>
                    </div>
                    <div name="sortColumn" class="modal-search-btn" data-value="9">
                        <div>Last Updated</div>
                    </div>
                    <div name="sortColumn" class="modal-search-btn" data-value="9" data-desc="true">
                        <div>Recently Updated</div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <%-- ----------- --%>
</div>
<script>
    // filterType 다중 검색을 위한 변수
    // 중복 제거를 위해 set 으로 작업 후 ajax 시 list 로 전달 진행
    let filterTypeSet = new Set();

    $('#searchValue').on('keyup', (e) => {
        search();
    })

    function search() {
        var search = {
            searchValue: $('#searchValue').val(),
            sortColumn: $('#sortColumn').val() * 1,
            filterTypeList: Array.from(filterTypeSet),   // 다중 검색을 위한 list 전달
            desc: $('#desc').val(),
            repositorySid: getStorage('repositorySid')
        }

        $.ajax({
            type: "post",
            url: '${contextPath}/projectRepository/ajax/${path}',
            data: search,
            success: function (result) {
                $('#ajax-container').html(result);
                // type, sort 검색 후 modal 을 닫기위함
                $('.close').click();
            },
            error: function (e) {
                console.log(e);
            }
        })
    }

    // UI 화면이 변경되었을 경우에도 modal 을 제위치에 출력하기 위해...
    setInterval(() => {
        setModalPosition('filterType');
        setModalPosition('sortColumn');
    })

</script>
<script type="text/javascript" src="${contextPath}/static/js/default/search.js"></script>