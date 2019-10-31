var registration = ( function() {
    return {
        getAttendees: function(){
            var that = this;
            let selection = $("select").val();
            let id = selection[selection.length - 1];
            
            $.ajax({
                url: 'registration?id=' + id,
                method: 'GET',
                dataType: 'html',
                success: function(response) {
                    that.showTable(response);                    
                }
            });
        },
        postAttendee : function(){
           var firstName = $("#firstName").val();
           var lastName = $("#lastName").val();
           var displayName = $("#displayName").val();
           var session = $("select").val();
           
           var data = firstName + ";" + lastName + ";" + displayName + ";" + session[session.length - 1];
           
           var that = this;
           
           $.ajax({
                url: 'registration?data='+data,
                method: 'POST',
                dataType: 'json',
                success: function(response) {
                    that.postAttendeeSuccess(response);                   
                }
            });
       },
       generateFullName: function(){
           var fullName = "";
           fullName += $("#firstName").val() + " " + $("#lastName").val();
           $("#displayName").val(fullName);        
       },       
       postAttendeeSuccess: function(response){
           var name = response.name;
           var id = response.regID;
           var text = "Congratulations! You have successfully registered as: " + name;
           text += "</br></br>Your registration code is: <b>"+id+"</b>";
           $("#output").html(text);
       },
        showTable: function(response){
            $("#output").html(response);
        }
    };
}());