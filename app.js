
/**
 * Module dependencies.
 */

var express = require('express')
, routes = require('./routes')
, http = require('http')
, ejs = require('ejs')
, bot = require('./routes/bot.js')
, socketsjs = require('./routes/sockets.js')
, path = require('path');

var app = express();

//all environments
app.set('port', process.env.PORT || 3000);
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));

//development only
if ('development' == app.get('env')) {
	app.use(express.errorHandler());
}

app.get('/', function(req, res){
	console.log('Request');
	res.render('index');
});

app.get('/refugee', function(req, res){
	res.sendfile('./views/refugee.html');
});

app.get('/volunteer', function(req, res){
	res.sendfile('./views/volunteer.html');
});

app.post('/update', socketsjs.update_list);

app.post('/list-refugees', routes.list_refugees);

app.get('/webhook/', bot.webhook_get);
app.post('/webhook/', bot.webhook_post);

var httpServer = http.createServer(app);
var io = require('socket.io')(httpServer);


socketsjs.socket_func(io);

httpServer.listen(app.get('port'), function(){
	console.log('Express server listening on port ' + app.get('port'));
});

bot.doSubscribeRequest();
