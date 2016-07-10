var socket_io;

exports.socket_func = function(io){
	socket_io = io;
	io.on('connection', function(socket){
		console.log('a user connected');
		socket.on('chat message', function(msg){
		    io.emit('chat message', msg);
		});
	});
};

exports.update_list = function(req, res){
	
	var id = req.body.refugeeID;
    var address = req.body.refugeeAddress;
    var phoneNumber = req.body.refugeePhone;
    
    var msg = {};
    msg.ID = id;
    msg.address=address;
    msg.phone = phoneNumber;
	
	socket_io.emit('add',msg);
	res.end();
};