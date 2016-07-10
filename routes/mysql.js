var ejs= require('ejs');
var mysql = require('mysql');

function getConnection(){
	
	var connection = mysql.createConnection({
	    host     : '127.0.0.1',
	    user     : 'root',
	    password : 'root',
	    database : 'heroku_f4cca122d17507a',
	    port	 : 3306
	});
	return connection;
}

function executeQuery(sqlQuery,callback){
	
	console.log("\nSQL Query::"+sqlQuery);
	
	var connection=getConnection();
	
	connection.query(sqlQuery, function(err, rows) {
		if(err){
			console.log("ERROR: " + err.message);
			callback(err, rows);
		}
		else 
		{	// return err or result
			//console.log("DB Results:"+rows);
			callback(err, rows);
		}
	});
	console.log("\nConnection closed..");
	connection.end();
}	


exports.executeQuery=executeQuery;