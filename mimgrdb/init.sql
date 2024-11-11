GRANT ALL PRIVILEGES ON mimgrdb.* TO 'mimgr'@'%';
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS mimgrdb;

USE mimgrdb;

CREATE TABLE IF NOT EXISTS users (
  id       INT         AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  role     CHAR(64)    DEFAULT "user",
  hash     CHAR(64)    NOT NULL,
  salt     CHAR(64)    NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
  category_id   INT           AUTO_INCREMENT PRIMARY KEY,
  parent_id     INT,
  category_name VARCHAR(100),
  FOREIGN KEY (parent_id) REFERENCES categories(category_id)
);

CREATE TABLE IF NOT EXISTS products (
  product_id     INT            AUTO_INCREMENT PRIMARY KEY,
  name           VARCHAR(255)   NOT NULL,
  price          DECIMAL(10, 2) NOT NULL,
  description    TEXT,
  stock_quantity INT            NOT NULL DEFAULT 0,
  category_id    INT,
  created_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  image_id       INT,
  FOREIGN KEY(category_id) REFERENCES categories(category_id)
);

CREATE TABLE IF NOT EXISTS images (
  image_id         INT          AUTO_INCREMENT PRIMARY KEY,
  image_url        VARCHAR(255) UNIQUE,
  image_name       VARCHAR(255),
  image_caption    TEXT,
  image_author     INT,
  image_created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (image_author) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS orders (
  order_id       INT           AUTO_INCREMENT PRIMARY KEY,
  order_status   VARCHAR(50)   NOT NULL,
  order_date     TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
  total_amount   DECIMAL(10,2) NOT NULL,
  payment_status VARCHAR(50)   NOT NULL
);

CREATE TABLE IF NOT EXISTS order_items (
  order_item_id INT AUTO_INCREMENT PRIMARY KEY,
  order_id      INT,
  product_id    INT,
  quantity      INT NOT NULL,
  unit_price    DECIMAL(10, 2) NOT NULL,
  total_price   DECIMAL(10, 2) NOT NULL,
  FOREIGN KEY (order_id) REFERENCES orders(order_id),
  FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE remember_me_tokens (
  token_id    VARCHAR(64) PRIMARY KEY,
  user_id     INT,
  token_value VARCHAR(64),
  expiration_time TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Add root categories first
INSERT INTO categories (category_id, category_name) VALUES 
  (1, 'Keyboards'),
  (2, 'Guitars'),
  (3, 'Basses'),
  (4, 'Drums & Percussion'),
  (5, 'String Instruments'),
  (6, 'Wind Instruments'),
  (7, 'Accessories'),
  (51, NULL);

-- Add subcategory level 1
INSERT INTO categories (category_name, parent_id) VALUES
  ('Acoustic pianos', 1), ('Grand pianos', 1), ('Electric pianos', 1), ('Stage Pianos', 1), ('Keyboards', 1), ('Master Keyboards', 1),
  ('Organs', 1), ('Accordions', 1), ('Synthesizers', 1), ('Modular Synthesizers', 1), ('MIDI Devices for Keyboard', 1),
  ('Flight Cases for Keyboad Instruments', 1), ('Bags for Keyboard Instruments', 1), ('Stands for Keyboard Instruments', 1),
  ('Benches', 1), ('Effect Devices for Keyboard Instruments', 1), ('Groove Tools', 1), ('Keyboard Amplifiers', 1),
  ('Cables for Keyboard Instruments', 1), ('Accessories for Keyboard Instruments', 1),  ('Synthesizers', 1),

  ('Electric Guitars', 2), ('Semi Acoustic Guitar', 2), ('Classical Guitars', 2), ('Acoustic Guitars', 2), ('Ukuleles', 2),
  ('Other String Instruments', 2), ('Electric Guitar Amps', 2), ('Acoustic Guitar Amps', 2), ('Modelling for Guitars', 2),
  ('Effects for Guitars', 2), ('Rack Equipment for Guitars', 2), ('Guitar Synths', 2), ('Wireless Transmission Systems Guitars', 2),
  ('Valves and Tubes for Amps', 2), ('Heavy Metal Shop', 2), ('Guitar Parts', 2), ('Strings for Guitars', 2), ('Electric Guitar Pickups', 2),
  ('Guitar Cases and Bags', 2), ('Accessories for Guitars', 2), ('Books & DVDs for guitars', 2), ('Cables for Guitars', 2),

  ('Electric Bass', 3), ('Semi Acoustic Bass', 3), ('Acoustic Bass', 3), ('Bass Ukulele', 3), ('Upright Basses', 3), ('Bass Amplifier', 3),
  ('Bass Cabinets', 3), ('Bass Effects', 3), ('Wireless Transmission Systems Bass', 3), ('Cable for Bass Guitar', 3), ('Bass Strings', 3),
  ('Bass Cases', 3), ('Bass Gig Bags', 3), ('Bass Parts', 3), ('Bass Pickups', 3), ('Bass Accessories', 3), ('Sheet Music for Bass', 3),

  ('Acoustic Drums', 4), ('E-Drums', 4), ('Cymbals', 4), ('Handpans and Steel Tongue Drums', 4), ('Percussion', 4), ('Drum Kit Hardware Parts', 4),
  ('Drums for Kids', 4), ('Marching Drums', 4), ('Orchestral Percussion', 4), ('Sticks, Beaters and Mallets', 4), ('Drumheads', 4),
  ('Bags and Cases for Drums', 4), ('Accessories for Drums', 4), ('Sheet music & DVDs for Drumming', 4),

  ('Violins', 5), ('Violas', 5), ('Cellos', 5), ('Double Basses', 5), ('Strings for String Instruments', 5),
  ('Bows for String Instruments', 5), ('Cases and Bags for String Instruments', 5), ('Accessories for String Instruments', 5),

  ('Recorders', 6), ('Harmonicas', 6), ('Professional Melodicas', 6), ('Flutes', 6), ('Marching Instruments', 6), ('Clarinets', 6),
  ('Saxophones', 6), ('Trumpets', 6), ('Flugelhorns', 6), ('Trombones', 6), ('Tenor Horns and Baritones', 6), ('Euphoniums', 6),
  ('Tubas', 6), ('French Horns', 6), ('Hunting Horns', 6), ('Pan Flutes and Effect Pipes', 6), ('Reeds for Wind Instruments', 6),
  ('Mouthpieces for Woodwind', 6), ('Mouthpieces for Brasswind', 6), ('Mutes for Winds', 6), ('Pickup Systems for Winds', 6),
  ('Accessories for Winds', 6), ('Music Books for Winds', 6),

  ('Headphones', 7), ('Cables', 7), ('Plugs & Adapters', 7), ('Stands', 7), ('Tuners', 7), ('Metronomes', 7), ('Switches & Buttons', 7),
  ('Power Adapters & Supplies', 7), ('Cable Clamps, Gaffa Tape, etc.', 7)
;

INSERT INTO users (username, hash, salt) VALUES ('ikienkinzero', 'bc85bbcceedd99b2b7713b2976046b099fb3649d029633803bdfc71c35603e9a', 'rduAoV20DQsHQtQrD0p3SQ==');
INSERT INTO users (username, hash, salt) VALUES ('nd2204', '4a6381563f67d178ef73a2ac4f4119978d2835dfd8f1ec8f67f78460618d93ae', '9NOV6F3hYrOS0HeanJMuDA==');
INSERT INTO users (username, hash, salt) VALUES ('buidinhhuy', 'e2b903a3eee033d9b05d525ccfd904a78365736c5b6f2da3a25e77e4cfbb12fe', 'kh9/vS/RNlKttgRU1uWDaw==');

INSERT INTO mimgrdb.products (name,price,description,stock_quantity,category_id,created_at,updated_at,image_id) VALUES
	 ('Kawai K-15E Piano Black Polished 112 cm',3890.00,'The Kawai K-15 e PE piano with an impeccable finish and a full, voluminous sound. This is largely due to the K-15''s solid soundboard, which is unique in this price range. This is made of spruce wood, which has been carefully selected and tested. By using very hard tuning pins made of maple, nickel-plated tuning pegs and stable detents made of plywood, the models of the K-series are especially tuning stable and allow the pianist decades of playing pleasure. The Kawai K-15 e PE piano also features the extremely precise Millenium action. The hammer heads are pressed from 100% premium wool.',15,52,'2024-11-11 02:29:18','2024-11-11 02:29:18',1),
	 ('Yamaha B1 PE Piano 109 cm Black polished',3990.00,'The b series, like all Yamaha pianos, are instruments of extraordinary, natural beauty, combining art, craftsmanship and technology; a pleasure to play and to own. Yamaha have been making pianos for over a century but the new b series is something of a departure for us. A hundred years of accumulated knowledge has been condensed into our most affordable piano ever. No corners have been cut. No compromises made in materials or workmanship. We simply set out to design fully-featured Yamaha piano but at a lower price. And we succeed. Yamaha b1 PE Piano- A perfect balance For the ambitious performer on a budget, there is no better instrument.

Undercutting lesser competitors whilst retaining Yamaha''s high standards, the b1 offers the joy of Yamaha ownership at a price that will astonish and delight. YamahaÂ´s most affordable piano ever. No corners have been cut. No compromises made in materials or workmanship. The b Series, like all Yamaha pianos, are instruments of extraordinary natural beauty, combining art, craftsmanship and technology; a pleasure to play and to own. The b1 is also available in a Silent Pianoâ„¢ option.',14,52,'2024-11-11 02:33:01','2024-11-11 02:33:01',2),
	 ('Schimmel F 116 FRIDOLIN',5490.00,'Features:
Manufacturer: Schimmel
Width (cm): 148
Height (cm): 116
Depth (cm): 59
Weight (kg): 230
Surface: High-gloss
Number of pedals: 3
Silent system: No
Wheels: Yes
Slides: No',12,52,'2024-11-11 02:34:46','2024-11-11 02:34:46',3),
	 ('Kawai GL- 10 E/P Grand',11990.00,'Kawai GL-10 E/P at a glance
The GL-10 E/P is a baby grand piano with a great sound, and Kawai has put the same emphasis on careful craftsmanship and high quality as they do on their larger grand pianos. The result is an instrument that is great to play, has a great grand piano sound, and still fits into almost any room.',10,53,'2024-11-11 02:35:58','2024-11-11 02:35:58',4),
	 ('Yamaha GB 1 K Grand Black polished 151cm 3 Pedals',13990.00,'Features:
Manufacturer: Yamaha
Width (cm): 146
Height (cm): 99
Depth (cm): 151
Weight (kg): 261
Surface: High-gloss
Number of pedals: 3
Silent system: No
Wheels: Yes
Slides: No',15,53,'2024-11-11 02:46:14','2024-11-11 02:46:14',5),
	 ('Schimmel C 213 Tradition Grand 213 cm Black polished',50300.00,'Features:
Manufacturer: Schimmel
Width (cm): 151
Height (cm): 101
Depth (cm): 213
Weight (kg): 390
Surface: High-gloss
Number of pedals: 3
Silent system: No
Wheels: Yes
Slides: No',12,53,'2024-11-11 02:47:45','2024-11-11 03:51:26',6),
	 ('keymaXX SDP-155 COMPLETE - Set',431.08,'The keymaXX SDP-155 COMPLETE - Set offers absolutely everything to guarantee beginners a successful start in piano lessons. With its authentic hammer action and first-class sounds, the keymaXX SDP-155 proves to be the ideal D piano for beginners and advanced players. The Fame PB-10C piano bench with continuously adjustable height ensures the correct sitting posture, while the MUSIC STORE MS-300 Deluxe headphones also enable silent practice of the first pieces. Last but not least, the two-volume sheet music collection MUSIC STORE Classics of Piano Music provides a good overview of the classical piano repertoire from the age of the Baroque to the 20th century.',16,54,'2024-11-11 02:49:04','2024-11-11 03:51:26',7),
	 ('Fame DP-2000 wh & K92 - Set',559.00,'Set: DP-2000 wh & K92 - Set

Set contains


1x Fame DP-2000 WH
1x AKG K 92',20,54,'2024-11-11 02:50:03','2024-11-11 03:51:26',8),
	 ('Yamaha YDP-145 WH & K92 - Set',910.01,'Set: YDP-145 WH & K92 - Set

Set contains


1x Yamaha YDP-145 WH
1x AKG K 92',22,54,'2024-11-11 02:52:03','2024-11-11 03:51:26',9),
	 ('Fame DP-88 + Bag - Set ',209.00,'Set: DP-88 + Bag - Set

Set contains


1x Fame DP-88 88-Note Stage Piano (Black)
1x Fame BAG-88 fÃ¼r DP-88
1x MUSIC STORE MS 300 Dynamic Headphons 32 Ohm, 102 dB, 20Hz-20KHz',23,55,'2024-11-11 02:53:11','2024-11-11 03:51:26',10);
INSERT INTO mimgrdb.products (name,price,description,stock_quantity,category_id,created_at,updated_at,image_id) VALUES
	 ('Casio CT-S1 WH & Bag - Set',239.00,'Set: CT-S1 WH & Bag - Set

Set contains


1x Casio CT-S1 WH inkl. TB-1A Sustainpedal
1x Casio SC-500 Keyboard Bag fÃ¼r 61 Tasten Casio GerÃ¤te',13,55,'2024-11-11 02:54:04','2024-11-11 03:51:26',11),
	 ('keymaXX SP-1 Complete-Set',269.00,'Set: SP-1 Complete-Set

Set contains


1x keymaXX SP-1 Digital Piano (Black)
1x MUSIC STORE KB-4 EX
1x MUSIC STORE MS 300 Dynamic Headphons 32 Ohm, 102 dB, 20Hz-20KHz',12,55,'2024-11-11 02:54:49','2024-11-11 03:51:26',12),
	 ('Yamaha CK61 - Stage-Set ',1038.99,'Set: CK61 - Stage-Set

Set contains


1x Yamaha CK61
1x MUSIC STORE OEM-AKS-1185 Table-Style Keyboard Stand (Black)
1x MUSIC STORE TB-1 Sustain Pedal (Black)
1x MUSIC STORE VM18L,Volume pedal,switchable',22,56,'2024-11-11 02:58:08','2024-11-11 03:51:26',13),
	 ('Roland VR-730 V-Combo - Stage Set',1349.00,'Set: VR-730 V-Combo - Stage Set

Set contains


1x Roland VR-730 V-Combo
1x MUSIC STORE OEM-AKS-1185 Table-Style Keyboard Stand (Black)
1x MUSIC STORE TB-1 Sustain Pedal (Black)
1x MUSIC STORE VM18L,Volume pedal,switchable',12,56,'2024-11-11 02:59:01','2024-11-11 03:51:26',14),
	 ('Korg Pa5X 61 Internat. & Case-Set ',4119.01,'Set: Pa5X 61 Internat. & Case-Set

Set contains


1x Korg Pa5X 61 International
1x BOXPROFI RS-Case Korg Pa5X 61',14,56,'2024-11-11 02:59:54','2024-11-11 03:51:26',15),
	 ('Miditech Midistart Music 25 + KCS Bag Set',78.99,'Set: Music 25 + KCS Bag Set

Set contains


1x Miditech Midistart Music 25 USB Masterkeyboard
1x MUSIC STORE Tasche KCS-I MaÃŸe: 45 x 25 x 9cm',20,57,'2024-11-11 03:00:57','2024-11-11 03:51:26',16),
	 ('Studiologic SL-88 Studio Basic Set',438.99,'Set: SL-88 Studio Basic Set

Set contains


1x Studiologic SL-88 Studio 88-Note Keyboard Controller (Black)
1x Studiologic SL Magnetic Music Stand',23,57,'2024-11-11 03:01:34','2024-11-11 03:51:26',17),
	 ('Kawai VPC 1 + Klapptisch -Set',1479.00,'Set: VPC 1 + Klapptisch -Set

Set contains


1x Kawai VPC1 Masterkeyboard
1x KÃ¶nig & Meyer 18953 Table-Style Stage Piano Stand (Black)
1x MUSIC STORE MS 300 Dynamic Headphons 32 Ohm, 102 dB, 20Hz-20KHz',11,57,'2024-11-11 03:02:22','2024-11-11 03:51:26',18),
	 ('Yamaha Reface YC',369.00,'The Reface YC Yamaha is a combo organ equipped with the sound of five different vintage organ models. The synthesizer delivers fat, punchy sounds and a unique expressiveness via nine drawbars, percussion, vibrato and rotary speaker effects. The organ sound is produced by a special Organ Flute sound generator and enriched by additional retro organ effects - Distortion and reverb. The Reface YC is a robustly constructed mini-organ with small keys and is able to be battery operated.',13,58,'2024-11-11 03:04:27','2024-11-11 03:51:26',19),
	 ('Ferrofish B4000+ Organ Expander',389.00,'The Ferrofish B4000+ Organ Expander is a quality Organ sound expander.

How good could an expander be with the new up to date technical possibilities? The most successful Organ expander in company history answers this very question. With a TFT Display, new processor, newest converter generation and a further improved sound! The draw bars for direct access to the register are naturally still available as well as the ultra robust housing.',14,58,'2024-11-11 03:07:48','2024-11-11 03:51:26',20);
INSERT INTO mimgrdb.products (name,price,description,stock_quantity,category_id,created_at,updated_at,image_id) VALUES
	 ('Crumar Mojo 61B Lower Manual',549.00,'The Crumar Mojo61B Additional Manual Keyboard for the Mojo61 is an optional lower manual keyboard that does not require an additional power supply or special tools to make the two keyboards stay together. The Mojo61 simply sits on top of the lower manual, aligned thanks to the rubber feet matching with holes on the bottom keyboard. A single special MIDI cable will connect both units for power and data. This optional lower manual keyboard can accomodate the optional Half-Moon Rotary Switch that is easily attached/detached by two hand screws and a TRS jack cable. You can use the "high trigger" point on the lower manual as well as on the upper manual.',23,58,'2024-11-11 03:08:44','2024-11-11 03:51:26',21),
	 ('Roland FR-1X Piano-Type V-Accordion Black',1699.00,'Features:
Manufacturer: Roland
Number of Basses: 72
Polyphony: 128
Integrated Effects Processor: Yes
USB MIDI Interface: Yes
Headphone Connection: Yes
Storage Media: USB Stick
LINE OUT: Yes
Speakers: Yes',23,59,'2024-11-11 03:11:10','2024-11-11 03:51:26',22),
	 ('Korg FISA SUPREMA Tastenversion',7979.00,'Experience accordion playing on a new level with the FISA SUPREMA from KORG. This innovative digital accordion has been developed over several years by a specialized team of accordion experts, engineers and sound designers to provide an unparalleled sound experience and authentic playing feel. With the "Advanced Proportional Bellows Behavior" technology, the FISA SUPREMA sets new standards. This technology provides a more realistic bellows behavior that far surpasses conventional digital accordions and enables almost infinitely variable dynamics. This allows special techniques, such as bellows shaking, to be played precisely and expressively.',22,59,'2024-11-11 03:12:00','2024-11-11 03:51:26',23),
	 ('Roland FR-4X BK',3749.00,'The Roland FR-4x is a compact and lightweight digital V-Accordion with premium characteristics to enhance your creativity wherever you are. The FR-4x is equipped with 37 keys and 120 left-hand buttons as well as the sound module and user interface from the iconic FR-8xb. A headphones jack enables quiet practice at home, whilst a powerful onboard audio system with two speakers and a tweeter provides the player with a dynamic full-range sound to monitor their playing. You can even plug this accordion directly into a PA system via the Line Out so you do not need to mic it up.',23,59,'2024-11-11 03:12:47','2024-11-11 03:51:26',24),
	 ('AKAI Professional MPK Mini Play Mk3',119.00,'Akai gives the musician a deliberately focused mobile and untethered solution with the MPK mini Play mk3. The transition between DAW controller and standalone device is meant here fluently: equipped with four AA batteries you can practice for hours at any place, try out and have fun. For this purpose, the MPK mini Play mk3 shines with 128 sounds and drum kits, newly developed Gen2 keyboard with significantly optimized response and better velocity, joystick controller, eight velocity-sensitive MPC pads, four assignable endless knobs, a programmable arpeggiator and an audibly improved speaker, which is not only louder than the previous model but also capable of more bass. When you connect the MPK mini Play mk3 to your computer, you enjoy all the benefits of a compact DAW controller. The internal sounds and effects can be output via the headphone output to a connected audio interface; the communication with the computer refers only to data respectively MIDI.',22,60,'2024-11-11 03:15:52','2024-11-11 03:51:26',25),
	 ('Korg Volca Drum',139.00,'The Korg Volca Drum is a drum computer with 6x tracks, 2-layer synthesis and waveguide resonator. This instrument creates varied percussion sounds on 6-parts, which are composed of 2x layers and go far beyond the sound palette of typical analogue drums. The digital DSP Synth Engine generates various waveforms, including noise, which can be pitch modulated, enveloped and bit crushed. This is followed by the Waveguide Resonator, which defines properties such as body shape, pitch, feedback and decay, giving the dual parts the finishing touches and transforming the sound to a new level.',24,60,'2024-11-11 03:16:37','2024-11-11 03:51:26',26),
	 ('Sonicware LIVEN Ambient',279.00,'The LIVEN Ambient Ã˜ from Sonicware is a digital synthesizer for ambient and drone sounds with a sequencer. The wavetable-based sound generation is based on six oscillator combinations with a fixed structure, which are ultimately used to create the sound. The fourth track, respectively Layer, is designed for noisy sounds and is the only layer that can sample itself and integrate the audio material into the musical creation process. Each of the four layers/tracks is equipped withits own envelope, multimode filter and two LFOs. Finally, there are two surprisingly good-sounding effect blocks. The sonic focus is clearly on layered, meandering sounds, which are particularly well suited to slow, atmospheric music and offer a high degree of musical usability in this context. Heavy staccato and baller basslines, on the other hand, are not the strength of this compact groovebox. The built-in loudspeaker and alternative battery operation (6x AA batteries) are ideal for anyone who wants to make music wherever they are. There are MIDI inputs and outputs as well as analogue Sync In & Out for connecting to other equipment and synchronizing the Groovebox with Korg Volca or Teenage Pocket Operators.',20,60,'2024-11-11 03:17:41','2024-11-11 03:51:26',27),
	 ('cre8audio Nifty Bundle',265.00,'The Nifty Bundle from Cre8audio is a compact Eurorack system that provides the user with plenty of space for his own equipment. Equipped with a high-performance power supply, a total of 84 HP space for modules, the duophonic MIDI interface and the audio summerizer, the case itself is a great addition to existing setups or the all-round carefree solution for beginners.',30,61,'2024-11-11 03:18:44','2024-11-11 03:51:26',28),
	 ('Behringer System 35',879.00,'Behringer System 35 at a glance
Eurorack Go Case
3x 902 VCA
1x 904A VC Lowpass filter
1x 904B VC Highpass Filter
3x 911 Envelope Generator
1x 914 Fixed Filter Bank
1x 921 VC Oscillator
2x 921A Oscillator Driver
4x 921B VCO
1x 923 Filters / Noise
1x 961 Interface
1x 992 Control Voltages
1x CM1A MIDI-CV/Gate
1x CP35 Attenuators
2x CP3A-M Mixer
2x CP3A-O Oscillator Controller
4x Dummy plates',33,61,'2024-11-11 03:19:28','2024-11-11 03:51:26',29),
	 ('Erica Synths Pico System II',1299.00,'The Pico System II from Erica Synths is a pre-configured modular system that moves between flexible synthesizer and compact groovebox. The 14 carefully selected modules are pre-installed in a Pico Case and can be used directly thanks to the 20 included patch cables.',34,61,'2024-11-11 03:20:04','2024-11-11 03:51:26',30);
INSERT INTO mimgrdb.products (name,price,description,stock_quantity,category_id,created_at,updated_at,image_id) VALUES
	 ('MUSIC STORE E-Gitarren ZubehÃ¶r Paket ',29.00,'Set: E-Gitarren ZubehÃ¶r Paket

Set contains


1x MUSIC STORE Gig-Bag Eco+ (Electric Guitar)
1x MUSIC STORE SKDG010 A-Frame Stand (Electric Guitar)
1x MUSIC STORE Instrument Cable Slim 90Â° 3m (Black)
1x Fame CAT-04C Clip-On Tuner
1x J & D NS5 Strap Nylon',43,73,'2024-11-11 03:22:02','2024-11-11 03:51:26',31),
	 ('J & D ST-MINI Sunburst - Set',99.00,'Set: ST-MINI Sunburst - Set

Set contains


1x J & D ST-MINI Sunburst
1x J & D NS5 Strap Nylon
1x MUSIC STORE Instrument Cable Standard 3m (Black)
1x MUSIC STORE SKDG010 A-Frame Stand (Electric Guitar)',43,73,'2024-11-11 03:22:51','2024-11-11 03:51:26',32),
	 ('Rockson TL E-Guitar Black + MS-2 WH ',149.00,'Set: TL E-Guitar Black + MS-2 WH

Set contains


1x Rockson TL (Black)
1x Marshall Ms-2 Micro Amp (Grey)
1x MUSIC STORE Guitar Accessories Pack (Electric Guitar)
1x Fame PBE0942 Peter Bursch Electric Guitar Strings',44,73,'2024-11-11 03:23:51','2024-11-11 03:51:26',33),
	 ('J & D SH 40 NT Natural, incl. Case',298.00,'The J&D SH-40 Electric Guitar, Natural is a beautifully crafted guitar which produces amazing tone and playability.

The SH-40 features a very familiar semi-hollow, Double Cut-away maple body with a flamed maple top. The maple neck has been topped with a rosewood fingerboard.

The J&D SH-40 has two ceramic humbucker pickups which really help to define its unique vintage tone. A three-way pickup switch is also on show to give the user the option of using the pickups independently or obtaining a mixture of both. The guitar also has die-cast machine heads, gold-coloured hardware and has been finished in a natural effect with a stylish â€˜F holesâ€™ design.',44,74,'2024-11-11 03:25:44','2024-11-11 03:51:26',34),
	 ('Squier Affinity Series Starcaster MN 3-Color Sunburst ',299.00,'Features:
Manufacturer: Squier
Series: Affinity
Design: Starcaster
Top: Maple Laminate
Back & Sides: Maple laminate
Solid Centre Block: Yes
Binding: 1-ply, Black
Neck: Maple
Neck Construction: Bolt-on
Neck Profile: ''C''
Fretboard: Maple
Fretboard Radius in Inches: 9.5
Fretboard Inlays: Dot
Number of Frets: 22
Fret Format: Narrow Tall
Nut type: PPS
Nut width in mm: 42
Scale in mm: 648
Pickup: HH
Manufacturer pickups: Squier
Neck Pickup: Standard Humbucker
Bridge Pickup: Standard Humbucker
Piezo Pickup: No
Active pickups: No
Coil-splitting: No
Switch: 3-Way
Control: Vol, Tone
Bridge: Adjusto-Matic
Tailpiece: Stopbar
Tuners: Die-Cast
Strap Button: Standard
Hardware: Chrome
Colour: 3-Color Sunburst
Finish: High-Gloss
Number of Strings: 6
String Gauge: .009 - .042
Factory Tuning: Standard E
Gigbag included: No
Case included: No
Certificate: No',45,74,'2024-11-11 03:26:40','2024-11-11 03:51:26',35),
	 ('Ibanez Artcore AS53-SRF Sunburst Red Flat',329.00,'The Ibanez Artcore AS53-SRF Sunburst Red Flat Semi Hollow from the Ibanez Artcore series combines the nimble doublecut design of the AM53 with a larger body volume and presents itself as a versatile electric guitar from jazzy cleans to classic Blues crunch to the gain-rich sound of modern rock.',41,74,'2024-11-11 03:27:14','2024-11-11 03:51:26',36);
INSERT INTO mimgrdb.images (image_url,image_name,image_caption,image_author,image_created_at) VALUES
	 ('uploads/kawai-k-15e-piano-black-polished-112-cm_1_PIA0000475-000_1.jpg','kawai-k-15e-piano-black-polished-112-cm_1_PIA0000475-000','',1,'2024-11-11 02:29:18'),
	 ('uploads/yamaha-b1-pe-piano-109-cm-black-polished_1_PIA0000776-000_1.jpg','yamaha-b1-pe-piano-109-cm-black-polished_1_PIA0000776-000','',1,'2024-11-11 02:33:01'),
	 ('uploads/schimmel-f-116-fridolin_1_PIA0002476-000_1.jpg','schimmel-f-116-fridolin_1_PIA0002476-000','',1,'2024-11-11 02:34:46'),
	 ('uploads/kawai-gl-10-e-p-grand_1_PIA0002124-000_1.jpg','kawai-gl-10-e-p-grand_1_PIA0002124-000','',1,'2024-11-11 02:35:58'),
	 ('uploads/yamaha-gb1-pwh-grand-150cm-white-polished_1_PIA0002058-000_1.jpg','yamaha-gb1-pwh-grand-150cm-white-polished_1_PIA0002058-000','',1,'2024-11-11 02:46:14'),
	 ('uploads/keymaxx-sdp-155-complete-set_1_EPI701_1_1.jpg','keymaxx-sdp-155-complete-set_1_EPI701_1','',1,'2024-11-11 02:49:31'),
	 ('uploads/fame-dp-2000-wh-und-k92-set_1_EPI618_1.jpg','fame-dp-2000-wh-und-k92-set_1_EPI618','',1,'2024-11-11 02:50:03'),
	 ('uploads/yamaha-ydp-145-wh-und-k92-set_1_EPI252_1.jpg','yamaha-ydp-145-wh-und-k92-set_1_EPI252','',1,'2024-11-11 02:52:03'),
	 ('uploads/fame-dp-88-bag-set_1_KEY012_1.jpg','fame-dp-88-bag-set_1_KEY012','',1,'2024-11-11 02:53:11'),
	 ('uploads/casio-ct-s1-wh-und-bag-set_1_KEY121_1.jpg','casio-ct-s1-wh-und-bag-set_1_KEY121','',1,'2024-11-11 02:54:04');
INSERT INTO mimgrdb.images (image_url,image_name,image_caption,image_author,image_created_at) VALUES
	 ('uploads/keymaxx-sp-1-complete-set_1_KEY615_1.jpg','keymaxx-sp-1-complete-set_1_KEY615','',1,'2024-11-11 02:54:49'),
	 ('uploads/schimmel-c-213-tradition-grand-213-cm-black-polished_1_PIA0000705-001_1_1.jpg','schimmel-c-213-tradition-grand-213-cm-black-polished_1_PIA0000705-001_1','',1,'2024-11-11 02:56:02'),
	 ('uploads/yamaha-ck61-stage-set_1_KEY251_1.jpg','yamaha-ck61-stage-set_1_KEY251','',1,'2024-11-11 02:58:08'),
	 ('uploads/roland-vr-730-v-combo-stage-set_1_KEY350_1.jpg','roland-vr-730-v-combo-stage-set_1_KEY350','',1,'2024-11-11 02:59:01'),
	 ('uploads/miditech-midistart-music-25-kcs-bag-set_1_SYN851_1.jpg','miditech-midistart-music-25-kcs-bag-set_1_SYN851','',1,'2024-11-11 03:00:57'),
	 ('uploads/studiologic-sl-88-studio-basic-set_1_SYN192_1.jpg','studiologic-sl-88-studio-basic-set_1_SYN192','',1,'2024-11-11 03:01:34'),
	 ('uploads/korg-pa5x-61-internat-und-case-set_1_KEY400_1_1.jpg','korg-pa5x-61-internat-und-case-set_1_KEY400_1','',1,'2024-11-11 03:03:03'),
	 ('uploads/kawai-vpc-1-klapptisch-set_1_SYN904_1_1.jpg','kawai-vpc-1-klapptisch-set_1_SYN904_1','',1,'2024-11-11 03:03:14'),
	 ('uploads/yamaha-reface-yc_1_SYN0004828-000_1.jpg','yamaha-reface-yc_1_SYN0004828-000','',1,'2024-11-11 03:04:27'),
	 ('uploads/ferrofish-b4000-organ-expander_1_KEY0003269-000_1.jpg','ferrofish-b4000-organ-expander_1_KEY0003269-000','',1,'2024-11-11 03:07:48');
INSERT INTO mimgrdb.images (image_url,image_name,image_caption,image_author,image_created_at) VALUES
	 ('uploads/crumar-mojo-61b-lower-manual_1_KEY0004634-000_1.jpg','crumar-mojo-61b-lower-manual_1_KEY0004634-000','',1,'2024-11-11 03:08:44'),
	 ('uploads/roland-fr-1x-piano-type-v-accordion-black-_1_KEY0003353-000_1.jpg','roland-fr-1x-piano-type-v-accordion-black-_1_KEY0003353-000','',1,'2024-11-11 03:11:10'),
	 ('uploads/korg-fisa-suprema-tastenversion_1_KEY0006033-000_1.jpg','korg-fisa-suprema-tastenversion_1_KEY0006033-000','',1,'2024-11-11 03:12:00'),
	 ('uploads/roland-fr-4x-bk_1_KEY0004535-000_1_1.jpg','roland-fr-4x-bk_1_KEY0004535-000_1','',1,'2024-11-11 03:13:15'),
	 ('uploads/akai-professional-mpk-mini-play-mk3_1_SYN0008141-000_1.jpg','akai-professional-mpk-mini-play-mk3_1_SYN0008141-000','',1,'2024-11-11 03:15:52'),
	 ('uploads/korg-volca-drum_1_SYN0006845-000_1.jpg','korg-volca-drum_1_SYN0006845-000','',1,'2024-11-11 03:16:37'),
	 ('uploads/sonicware-liven-ambient_1_SYN0009044-000_1.jpg','sonicware-liven-ambient_1_SYN0009044-000','',1,'2024-11-11 03:17:41'),
	 ('uploads/cre8audio-nifty-bundle_1_SYN0007268-000_1.jpg','cre8audio-nifty-bundle_1_SYN0007268-000','',1,'2024-11-11 03:18:44'),
	 ('uploads/behringer-system-35_1_SYN0008001-000_1.jpg','behringer-system-35_1_SYN0008001-000','',1,'2024-11-11 03:19:28'),
	 ('uploads/erica-synths-pico-system-ii_1_SYN0007559-000_1.jpg','erica-synths-pico-system-ii_1_SYN0007559-000','',1,'2024-11-11 03:20:04');
INSERT INTO mimgrdb.images (image_url,image_name,image_caption,image_author,image_created_at) VALUES
	 ('uploads/music-store-e-gitarren-zubehoer-paket_1_GIT002_1.jpg','music-store-e-gitarren-zubehoer-paket_1_GIT002','',1,'2024-11-11 03:22:02'),
	 ('uploads/j-und-d-st-mini-sunburst-set_1_AAB540_1.jpg','j-und-d-st-mini-sunburst-set_1_AAB540','',1,'2024-11-11 03:22:51'),
	 ('uploads/rockson-tl-e-guitar-black-ms-2-wh_1_GIT423_1.jpg','rockson-tl-e-guitar-black-ms-2-wh_1_GIT423','',1,'2024-11-11 03:23:51'),
	 ('uploads/j-und-d-sh-40-nt-natural-incl-case_1_GIT0016578-000_1.jpg','j-und-d-sh-40-nt-natural-incl-case_1_GIT0016578-000','',1,'2024-11-11 03:25:44'),
	 ('uploads/squier-affinity-series-starcaster-mn-3-color-sunburst_1_GIT0050680-000_1.jpg','squier-affinity-series-starcaster-mn-3-color-sunburst_1_GIT0050680-000','',1,'2024-11-11 03:26:40'),
	 ('uploads/ibanez-artcore-as53-srf-sunburst-red-flat_1_GIT0057757-000_1.jpg','ibanez-artcore-as53-srf-sunburst-red-flat_1_GIT0057757-000','',1,'2024-11-11 03:27:14');
