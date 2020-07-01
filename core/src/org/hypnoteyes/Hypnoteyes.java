package org.hypnoteyes;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Hypnoteyes extends Game implements Helpers {
	static Music music;
	static Level[] levels;

	@Override
	public void create() {
		music = Gdx.audio.newMusic(Gdx.files.internal("ruskerdax-pondering_the_cosmos.ogg"));

		levels = new Level[]{
			new Level( // flat
				0,
				seq(seq(vec(-0.1,22.6), vec(-0.1,2), vec(40.1,2), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(32,2),
				seq(vec(10,3)),
				vec(20,15)
			),
			new Level( // bumpy flat
				1,
				seq(seq(vec(-0.1, 22.6), vec(-0.1, 3), vec(2, 2.5), vec(10, 3), vec(15, 2), vec(20, 4),
					vec(30, 2), vec(39, 2), vec(40.1, 3), vec(40.1, 22.6), vec(-0.1, 22.6))),
				vec(35, 2),
				seq(vec(2, 5), vec(3, 5), vec(4, 5)),
				vec(20, 15)
			),
			new Level( // descent
				2,
				seq(seq(vec(-0.1,22.6), vec(-0.1,18), vec(3,16), vec(6,17/*19*/), vec(30,2), vec(40.1,2), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(35,2),
				seq(vec(1,20), vec(2,20), vec(3,20), vec(4,20), vec(5,20)),
				vec(20,18)
			),
			new Level( // ascent
				3,
				seq(seq(vec(-0.1,22.6), vec(-0.1,3), vec(4,2), vec(30,16), vec(40.1,16), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(35,16),
				seq(vec(1,5), vec(2,5), vec(3,5), vec(4,5), vec(5,5)),
				vec(20,20)
			),
			new Level( // canyon
				0,
				seq(seq(vec(-0.1,22.6), vec(-0.1,10), vec(2,10), vec(8,6), vec(16,4), vec(18,2.5), vec(22, 2), vec(25,6), vec(25.5,5.5),
					vec(28,9), vec(28.5,8.5), vec(31,13), vec(31.5,12.5), vec(35,17), vec(40.1,17), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(1,10),
				seq(vec(35.5,18), vec(36.5,18), vec(37.5,18), vec(38.5,18), vec(39.5,18),
					vec(35,18), vec(36,18), vec(37,18), vec(38,18), vec(39,18)),
				vec(20,15)
			),
			new Level( // fence
				1,
				seq(seq(vec(-0.1,22.6), vec(-0.1,2), vec(19.5,2), vec(20,3), vec(20.5,2), vec(40.1,2), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(39,2),
				seq(vec(8,3), vec(9,3), vec(10,3), vec(11,3), vec(12,3)),
				vec(20,15)
			),
			new Level( // the monolith
				2,
				seq(seq(vec(-0.1,22.6), vec(-0.1,2), vec(40.1,2), vec(40.1,22.6), vec(-0.1,22.6)),
					seq(vec(18.5,8), vec(21.5,8), vec(21.5,2.8), vec(18.5,2.8), vec(18.5,8))),
				vec(30,2),
				seq(vec(3,3), vec(4,3), vec(5,3), vec(6,3), vec(7,3), vec(8,3), vec(9,3), vec(10,3), vec(11,3), vec(12,3)),
				vec(20,15)
			),
			new Level( // the jump
				3,
				seq(seq(vec(-0.1,22.6), vec(-0.1,5), vec(18.9,5.3), vec(18.9,2), vec(21.1,2),
					vec(21.1,5), vec(40.1,5), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(30,5),
				seq(vec(8,6), vec(9,6), vec(10,6), vec(11,6), vec(12,6)),
				vec(20,15)
			),
			new Level( // halfpipe
				0,
				seq(seq(vec(-0.1,22.6), vec(-0.1,8.000), vec(11.000,8.000), vec(11.049,7.374), vec(11.196,6.764), vec(11.436,6.184),
					vec(11.764,5.649), vec(12.172,5.172), vec(12.649,4.764), vec(13.184,4.436), vec(13.764,4.196), vec(14.374,4.049),
					vec(15.000,4.000), vec(25.000,4.000), vec(25.626,4.049), vec(26.236,4.196), vec(26.816,4.436), vec(27.351,4.764),
					vec(27.828,5.172), vec(28.236,5.649), vec(28.564,6.184), vec(28.804,6.764), vec(28.951,7.374), vec(29.000,8.000),
					vec(40.1,8.000), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(32,8),
				seq(vec(3,9)),
				vec(20,15)
			),
			new Level( // cave
				1,
				seq(seq(vec(-0.1,22.6), vec(-0.1,2), vec(38,2), vec(38, 15), vec(21.5,15),
					vec(21.5,2.8), vec(18.5,2.8), vec(18.5,22.6), vec(-0.1,22.6))),
				vec(30,2),
				seq(vec(3,3)),
				vec(4,15)
			),
			new Level( // aim
				2,
				seq(seq(vec(-0.1,22.6), vec(-0.1,10), vec(18,10), vec(18,2), vec(22,2), vec(22,8),
					vec(26,5), vec(35,5), vec(35,9), vec(22, 9),	vec(22,22.6), vec(-0.1,22.6))),
				vec(30,5),
				seq(vec(10,11)),
				vec(20,15)
			),
			new Level( // mountain
				3,
				seq(seq(vec(-0.1,22.6), vec(-0.1,2), vec(3,2), vec(19,18), vec(21, 18), vec(37, 2), vec(40.1,2), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(20,18),
				seq(vec(1,6), vec(2,6), vec(3,6), vec(4,6), vec(5,6), vec(35,6), vec(36,6), vec(37,6), vec(38,6), vec(39,6)),
				vec(5,18)
			),
			new Level( // crevice
				0,
				seq(seq(vec(-0.1,22.6), vec(-0.1,16), vec(8,15), vec(16,18), vec(24,16), vec(32,17), vec(36,8), vec(26,6), vec(18,7),
					vec(7,6), vec(-0.1,8), vec(-0.1,3), vec(4,3), vec(15,2), vec(40.1,2), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(2,3),
				seq(vec(1,20), vec(2,20), vec(3,20), vec(4,20), vec(5,20), vec(6,20), vec(7,20), vec(8,20), vec(9,20), vec(10,20)),
				vec(38,20)
			),
			new Level( // other side
				1,
				seq(seq(vec(-0.1, 22.6), vec(-0.1, 13), vec(2, 12.5), vec(10, 13), vec(15, 12), vec(20, 14), vec(30, 12),
					vec(39, 12), vec(40.1,13), vec(40.1, 22.6), vec(45,22.6), vec(45,-5), vec(-5,-5), vec(-5,22.6), vec(-0.1,22.6))),
				vec(35, 12),
				seq(vec(1,15), vec(2,15), vec(3,15), vec(4,15), vec(5,15)),
				vec(20, 5)
			),
			new Level( // the eye
				2,
				seq(
					seq(vec(27.071,11.250), vec(26.523,11.759), vec(25.938,12.225), vec(25.320,12.646),	vec(24.673,13.020), vec(23.999,13.345),
						vec(23.303,13.618), vec(22.588,13.838), vec(21.859,14.005), vec(21.120,14.116), vec(20.374,14.172), vec(19.626,14.172),
						vec(18.880,14.116), vec(18.141,14.005), vec(17.412,13.838), vec(16.697,13.618), vec(16.001,13.345), vec(15.327,13.020),
						vec(14.680,12.646), vec(14.062,12.225), vec(13.477,11.759), vec(12.929,11.250), vec(13.477,10.741), vec(14.062,10.275),
						vec(14.680,9.854), vec(15.327,9.480), vec(16.001,9.155), vec(16.697,8.882), vec(17.412,8.662), vec(18.141,8.495), vec(18.880,8.384),
						vec(19.626,8.328), vec(20.374,8.328), vec(21.120,8.384), vec(21.859,8.495), vec(22.588,8.662), vec(23.303,8.882), vec(23.999,9.155),
						vec(24.673,9.480), vec(25.320,9.854), vec(25.938,10.275), vec(26.523,10.741), vec(27.071,11.250)),
					seq(vec(31.000,11.250), vec(30.940,12.400), vec(30.760,13.537), vec(30.462,14.649), vec(30.049,15.724), vec(29.526,16.750), vec(28.899,17.716),
						vec(28.175,18.610), vec(27.360,19.425), vec(26.466,20.149), vec(25.500,20.776), vec(24.474,21.299), vec(23.399,21.712), vec(22.287,22.010),
						vec(21.150,22.190), vec(20.000,22.250), vec(18.850,22.190), vec(17.713,22.010), vec(16.601,21.712), vec(15.526,21.299), vec(14.500,20.776),
						vec(13.534,20.149), vec(12.640,19.425), vec(11.825,18.610), vec(11.101,17.716), vec(10.474,16.750), vec(9.951,15.724), vec(9.538,14.649), vec(9.240,13.537),
						vec(9.060,12.400), vec(9.000,11.250), vec(9.060,10.100), vec(9.240,8.963), vec(9.538,7.851), vec(9.951,6.776), vec(10.474,5.750), vec(11.101,4.784),
						vec(11.825,3.890), vec(12.640,3.075), vec(13.534,2.351), vec(14.500,1.724), vec(15.526,1.201), vec(16.601,0.788), vec(17.713,0.490), vec(18.850,0.310),
						vec(20.000,0.250), vec(21.150,0.310), vec(22.287,0.490), vec(23.399,0.788), vec(24.474,1.201), vec(25.500,1.724), vec(26.466,2.351), vec(27.360,3.075),
						vec(28.175,3.890), vec(28.899,4.784), vec(29.526,5.750), vec(30.049,6.776), vec(30.462,7.851), vec(30.760,8.963), vec(30.940,10.100), vec(31.000,11.250))),
				vec(20,0.3),
				seq(vec(20,21.5), vec(20,21), vec(20,20.5), vec(20,20), vec(20,19.5), vec(20,19), vec(20,18.5), vec(20,18), vec(20,17.5), vec(20,17)),
				vec(20,12)
			),
			new Level( // slippery slope
				3,
				seq(seq(vec(-0.1,22.6), vec(-0.1,0.5), vec(1.5,0.5), vec(1.5,5), vec(20, 8),
					vec(40.1,8), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(32,8),
				seq(vec(6,8), vec(7,8.2), vec(8,8.4) , vec(9,8.6), vec(10,8.8)),
				vec(20,15)
			),
			new Level( // race
				0,
				seq(seq(vec(-0.1,22.6), vec(-0.1,13.5), vec(17,13.5), vec(17,13), vec(-0.1,13),	vec(-0.1,5),
					vec(27,5), vec(30,0.5), vec(31.5,0.5), vec(28.5,5), vec(40.1,7.5), vec(40.1,11),
					vec(30.5,14), vec(40.1,17), vec(40.1,22.6), vec(24.5,22.6),	vec(34,18.5), vec(24.5,15.5),
					vec(24.5,12.5), vec(34,9.5), vec(4,9.5), vec(4,10), vec(21,10), vec(21,16.5),
					vec(4,16.5), vec(4,17), vec(21,17), vec(21,25.6), vec(-0.1,22.6))),
				vec(20,5),
				seq(vec(27,22)),
				vec(19,21)
			),
			new Level( // double slippery slope
				1,
				seq(seq(vec(-0.1,22.6), vec(-0.1,0.5), vec(1.5,0.5), vec(1.5,6), vec(19, 8), vec(21, 8),
					vec(38.5,6), vec(38.5,0.5), vec(40.1,0.5), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(20,8),
				seq(vec(8.5,8.5), vec(28.5,8.5)),
				vec(20,21)
			),
			new Level( // stairs
				2,
				seq(seq(vec(-0.1,22.6), vec(-0.1,2), vec(10,2), vec(11.2,4), vec(15,4), vec(16.2,6),
					vec(20,6), vec(21.2,8), vec(25,8),	vec(26.2,10), vec(30,10), vec(31.2,12), vec(35,12),
					vec(35,8), vec(20,4), vec(20,2), vec(40.1,2), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(22,2),
				seq(vec(1,3)),
				vec(20,15)
			),
			new Level( // sacrifice
				3,
				seq(seq(vec(-0.1,22.6), vec(-0.1,1.5), vec(1,1.5), vec(1,3.5), vec(6,2.5), vec(20.5,17), vec(20.5,19),
					vec(23,18), vec(13,8), vec(23,8), vec(35,7), vec(40.1,3), vec(40.1,22.6), vec(-0.1,22.6))),
				vec(22,8),
				seq(vec(17,14), vec(23.5,9), vec(24.5,9), vec(25.5,9), vec(26.5,9)),
				vec(20,13)
			)
		};

		setScreen(new TitleScreen(this));
	}
}