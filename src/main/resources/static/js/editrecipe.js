 $(document).ready(function() {
    $('#loader').hide();
    $("#save").on("click", function() {
    	$("#save").prop("disabled", true);
    	var name = $("#name").val();
        var file = $("#image").val(); 
        var category = $("#category").val();
        var description = $("#description").val();
        var form = $("#form").serialize();
    	var data = new FormData($("#form")[0]);

    	//alert(data);
        $('#loader').show();
        if (name === "" || category === "" || description === "") {
        	$("#save").prop("disabled", false);
            $('#loader').hide();
            $("#name").css("border-color", "red");
            $("#category").css("border-color", "red");
            $("#description").css("border-color", "red");
            
            $("#error_name").html("Please fill the required field.");
            $("#error_category").html("Please fill the required field.");
            $("#error_description").html("Please fill the required field.");
      
        } else {
            $("#name").css("border-color", "");
            $("#image").css("border-color", "");
            $("#category").css("border-color", "");
            $("#description").css("border-color", "");
            
            $('#error_name').css('opacity', 0);
            $('#error_image').css('opacity', 0);
            $('#error_category').css('opacity', 0);
            $('#error_description').css('opacity', 0);

                    $.ajax({
                        type: 'POST',
                        enctype: 'multipart/form-data',
                        data: data,
                        url: "/saveRecipeDetails", 
                        processData: false,
                        contentType: false,
                        cache: false,
                        success: function(data, statusText, xhr) {
                        console.log(xhr.status);
                        if(xhr.status == "200") {
                        	$('#loader').hide(); 
                        	$("#form")[0].reset();
                        	$('#success').css('display','block');
                            $("#error").text("");
                            $("#success").html("Congratulation, save was successful!");
                            $('#success').delay(2000).fadeOut('fast');
                         }	   
                        },
                        error: function(e) {
                        	$('#loader').hide();
                        	$('#error').css('display','block');
                            $("#error").html("Oh no! Something is wrong.");
                            $('#error').delay(5000).fadeOut('slow');
                            location.reload();
                        }
                    });
        }
            });
        });