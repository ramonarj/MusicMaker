(
a = MusicMaker.new;
OSCdef.new(\Init,{
	arg msg;
	a.init(msg[1]);
},'/Init', nil, 57120);
)
