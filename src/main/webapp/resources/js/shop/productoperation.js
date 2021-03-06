$(function () {
    var productId = getQueryString('productId');
    var infoUrl = '/shopadmin/getproductbyid?productId=' + productId;
    var categoryUrl ='/shopadmin/getproductcategorylist';
    var productPostUrl = '/shopadmin/modifyproduct';
    var isEdit = false;
    if(productId){
        getInfo(productId);
        isEdit = true;
    }else {
        getCategory();
        productPostUrl = '/shopadim/addproduct';
    }
    //获取需要编辑的商品的商品信息，并赋值给表单
    function getInfo(id) {
        $.getJSON(infoUrl,function (data){
            if(data.success){
                //从返回的JSON当中获取product对象信息，并赋值给表单
                var product = data.product;
                $('#product-name').val(product.productName);
                $('#product-desc').val(product.productDesc);
                $('#priority').val(product.priority);
                $('#normal-price').val(product.normalPrice);
                $('#promotion-price').val(product.promotionPrice);
                //获取原本的商品类别以及该店铺的所有商品类别列表
                var optionHtml = '';
                var optionArr = data.productCategoryList;
                var optionSeleted = product.productCategory.productCategoryId;
                optionArr.map(function(item,index) {
                    var isSelect = optionSeleted === item.productCategoryId ? 'seleted':'';
                    optionHtml += '<option data-value="'
                    + item.productCategoryId
                    + '"'
                    +isSelect
                    + '>'
                    + item.productCategoryName
                    +'</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }
    function getCategory() {
        $.getJSON(categoryUrl, function(data){
            if (data.success) {
                var productCategoryList = data.data;
                var optionHtml = '';
                productCategoryList
                    .map(function(item,index) {
                    optionHtml += '<option data-value="'
                        + item.productCategoryId + '">'
                        + item.productCategoryName + '</option>';
                });
                $('#category').html(optionHtml);
            }
        });
    }
    //提交按钮的事件响应，分别对商品添加和编辑操作做不同响应

    $('#submit').click(
        function() {
            var product = {};
            product.productName = $('#product-name').val();
            product.productDesc = $('#product-desc').val();
            product.priority = $('#priority').val();
            product.normalPrice = $('#normal-price').val();
            product.promotionPrice = $('#promotion-price').val();
            product.productCategory = {
                productCategoryId : $('#category').find('option').not(
                    function() {
                        return !this.selected;
                    }).data('value')
            };
            product.productId = productId;

            var thumbnail = $('#small-img')[0].files[0];
            console.log(thumbnail);
            var formData = new FormData();
            formData.append('thumbnail', thumbnail);
            $('.detail-img').map(
                function(index, item) {
                    if ($('.detail-img')[index].files.length > 0) {
                        formData.append('productImg' + index,
                            $('.detail-img')[index].files[0]);
                    }
                });
            formData.append('productStr', JSON.stringify(product));
            console.log(formData.get('productStr'));
            var verifyCodeActual = $('#j_captcha').val();
            if (!verifyCodeActual) {
                $.toast('请输入验证码！');
                return;
            }
            formData.append("verifyCodeActual", verifyCodeActual);
            $.ajax({
                url : productPostUrl,
                type : 'POST',
                data : formData,
                contentType : false,
                processData : false,
                cache : false,
                success : function(data) {
                    if (data.success) {
                        $.toast('提交成功！');
                        $('#captcha_img').click();
                    } else {
                        $.toast('提交失败！');
                        $('#captcha_img').click();
                    }
                }
            });
        });
    });