$(function () {
    var listUrl = '/shopadmin/getproductlistbyshop?pageIndex=1&pageSize=999';
    //商品下架URL
    var statusUrl = '/shopadmin/modifyproduct';
    getList();

    /**获取此店铺下的商品列表
     */
    function getList() {
        $.getJSON(listUrl, function (data) {
            if (data.success) {
                var productList = data.productList;
                var tempHtml = '';
                productList.map(function (item, index) {
                    var textOp = "下架";
                    var contraryStatus = 0;
                    if (item.enableStatus == 0) {
                        textOp = "上架";
                        contraryStatus = 1;
                    } else {
                        contraryStatus = 0;
                    }
                    tempHtml += '' + '<div class="row row-product">'
                        + '<div class="col-30">'
                        + item.productName
                        + '</div>'
                        + '<div class="col-20">'
                        + item.priority
                        + '</div>'
                        + '<div class="col-50">'
                        + '<a href="#" class="edit" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">编辑</a>'
                        + '<a href="#" class="status" data-id="'
                        + item.productId
                        + '" data-status="'
                        + contraryStatus
                        + '">'
                        + textOp
                        + '</a>'
                        + '<a href="#" class="preview" data-id="'
                        + item.productId
                        + '" data-status="'
                        + item.enableStatus
                        + '">预览</a>'
                        + '</div>'
                        + '</div>';
                });
                $('.product-wrap').html(tempHtml);
            }
        });
    }
    //将class为product-wrap里面的标签a绑定上点击事件
    $('.product-wrap').on('click','a',function (e) {
        var target = $(e.currentTarget);
        if(target.hasClass('edit')){
            //如果有class edit则点击进入店铺信息编辑页，并带有producyId参数
            window.location.href ='/shopadmin/productoperation?productId='
                + e.currentTarget.dataset.id;
        }else if(target.hasClass('status')){
            //如果有class status则调用后台功能上/下架相关商品，并带有productId 参数
            changeItemStatus(e.currentTarget.dataset.id,e.currentTarget.dataset.status);
        }else if (target.hasClass('preview')) {
            //如果有class preview则去前台展示系统该商品详情页预览商品情况
            window.location.href = '/frontend/productdetail?productId='
                + e.currentTarget.dataset.id;
        }
    });
    function changeItemStatus(id,enableStatus) {
        //定义product json对象并添加productId以及状态（上/下架）
        var product = {};
        product.productId = id;
        product.enableStatus = enableStatus;


        $.confirm('确定码？',function () {
            //上下架相关商品
            $.ajax({
                url:statusUrl,
                type:'POST',
                data:{
                    productStr:JSON.stringify(product),
                    statusChange:true
                },
                dataType:'json',
                success : function(data) {
                    if(data.success){
                        $.toast('操作成功！');
                        getList();
                    }else {
                        $.toast('操作失败！');
                    }
                }
            });
        })
    }
});