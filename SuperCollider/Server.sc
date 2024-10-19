
PluginServer { var <server, <>boot;

	init {
		var s;

		s = Server.local;
		s.options.numOutputBusChannels_(2);
		s.options.sampleRate_(44000);
		s.options.memSize_(2.pow(20));
		s.newBusAllocators;
		ServerBoot.removeAll;
		ServerTree.removeAll;
		ServerQuit.removeAll;
		server = s;

		// GLOBAL VARIABLES INITIALIZATION
		// ---------------------------------------------------
		// ---------------------------------------------------
		~out = 0;
		//~path = PathName(thisProcess.nowExecutingPath).parentPath++"buffers/";
		~path = "C:/ProgramData/SuperCollider/Extensions/buffers/";
		// FUNCTIONS DEFINITIONS
		// ---------------------------------------------------
		// CREAMOS LAS DISTINTAS FUNCIONES QUE QUEREMOS QUE SE LLAMEN AL HACER BOOT DEL SERVIDOR
		// ---------------------------------------------------

		// BUFFERS
		// ---------------------------------------------------
		~makeBuffers = {
			~buff = Dictionary.new;
			PathName(~path).entries.do{
				arg subfolder;
				~buff.add(
					subfolder.folderName.asSymbol ->
					Array.fill(
						subfolder.entries.size,
						{
							arg i;
							Buffer.read(s, subfolder.entries[i].fullPath);
						}
					)
				);
			};
		};

		// b[\folder][index].play;

		// BUSSES
		// ---------------------------------------------------
		~makeBusses = {
			~bus = Dictionary.new;
			~bus.add(\reverb -> Bus.audio(s,2));
		};

		// NODES
		// ---------------------------------------------------
		~makeNodes = {
			s.bind({
				~mainGrp = Group.new;
				~reverbGrp = Group.after(~mainGrp);
				~reverbSynth = Synth.new(
					\reverb,
					[
						\amp, 1,
						\predelay, 0.1,
						\revtime, 1.8,
						\lpf, 4500,
						\mix, 0.75,
						\in, ~bus[\reverb],
						\out, ~out,
					],
					~reverbGrp
				);
			});
		};

		// CLEANING
		// ---------------------------------------------------
		~cleanup = {
			s.newBusAllocators;
			ServerBoot.removeAll;
			ServerTree.removeAll;
			ServerQuit.removeAll;
		};

		// FUNCTIONS REGISTRATIONS W/SERVER
		// ---------------------------------------------------
		// ---------------------------------------------------

		ServerBoot.add(~makeBuffers);
		ServerBoot.add(~makeBusses);

		//Compilamos los sintetizadores
		ServerBoot.add(~synths);
		//Compilamos y generamos acorder
		ServerBoot.add(~effects);

		//Compilamos el paquete seleccionado, habra que ir añadiendo los paquetes conforme se vayan creando
		//ServerBoot.add(~ambientEvents);
		//ServerBoot.add(~terrorEvents);


		ServerQuit.add(~cleanup);

	}
}