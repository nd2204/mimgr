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

INSERT INTO products (name, price, stock_quantity, category_id, description) VALUES
  (
  'Kawai K-15E Piano Black Polished 112 cm', 3890.00, 10, 52,
  'Set: E-Gitarren Zubehör Paket Set contains 1x MUSIC STORE Gig-Bag Eco+ (Electric Guitar) 1x MUSIC STORE SKDG010 A-Frame Stand (Electric Guitar) 1x MUSIC STORE Instrument Cable Slim 90° 3m (Black) 1x Fame CAT-04C Clip-On Tuner 1x J & D NS5 Strap Nylon'
  ),
  (
  'Yamaha B1 PE Piano 109 cm Black polished', 3990.00, 10, 52,
  'The b series, like all Yamaha pianos, are instruments of extraordinary, natural beauty, combining art, craftsmanship and technology; a pleasure to play and to own. Yamaha have been making pianos for over a century but the new b series is something of a departure for us. A hundred years of accumulated knowledge has been condensed into our most affordable piano ever. No corners have been cut. No compromises made in materials or workmanship. We simply set out to design fully-featured Yamaha piano but at a lower price. And we succeed. Yamaha b1 PE Piano- A perfect balance For the ambitious performer on a budget, there is no better instrument.'
  ),
  (
  'Schimmel F 116 FRIDOLIN', 5490.00, 10, 52,
  'Features: Manufacturer: Schimmel Width (cm): 148 Height (cm): 116 Depth (cm): 59 Weight (kg): 230 Surface: High-gloss Number of pedals: 3 Silent system: No Wheels: Yes Slides: No'
  ),
  (
  'Kawai GL- 10 E/P Grand', 11990.00, 10, 53,
  'The GL-10 E/P is a baby grand piano with a great sound, and Kawai has put the same emphasis on careful craftsmanship and high quality as they do on their larger grand pianos. The result is an instrument that is great to play, has a great grand piano sound, and still fits into almost any room.'
  ),
  (
  'Yamaha GB 1 K Grand Black polished 151cm 3 Pedals', 12490.00, 10, 53,
  'Der schöne GB1 mit dem außergewöhnlichen Design-Konzept der begehrten Yamaha C-Serie erzeugt einen super Klang über den gesamten Dynamikbereich.Dank seiner kostensparenden Vorteile und einer beträchtlichen Verfeinerung der Materialien und der Herstellungstechnik ist dieses ausdrucksstarke und besonders preisgünstige Instrument jetzt besser den je.'
  ),
  (
  'Bechstein W. Hoffmann 190, Bj.´83 Snr. 128280, 3 Jahre Garantie', 21900.00, 10, 53,
  'This grand piano (still) comes from "Feurich" and was made in Langlau. At 190 cm, it is the perfect parlor size. Typical German manufacturing quality, Renner action, Kluge keyboard and the best workmanship and tonewood materials. A great instrument for relatively little money!'
  ),
  (
  'keymaXX SDP-155 COMPLETE - Set', 413.08, 10, 54,
  'The keymaXX SDP-155 COMPLETE - Set offers absolutely everything to guarantee beginners a successful start in piano lessons. With its authentic hammer action and first-class sounds, the keymaXX SDP-155 proves to be the ideal D piano for beginners and advanced players. The Fame PB-10C piano bench with continuously adjustable height ensures the correct sitting posture, while the MUSIC STORE MS-300 Deluxe headphones also enable silent practice of the first pieces. Last but not least, the two-volume sheet music collection MUSIC STORE Classics of Piano Music provides a good overview of the classical piano repertoire from the age of the Baroque to the 20th century.'
  ),
  (
  'Bechstein W. Hoffmann 190, Bj.´83 Snr. 128280, 3 Jahre Garantie', 21900.00, 10, 54,
  'This grand piano (still) comes from "Feurich" and was made in Langlau. At 190 cm, it is the perfect parlor size. Typical German manufacturing quality, Renner action, Kluge keyboard and the best workmanship and tonewood materials. A great instrument for relatively little money!'
  ),
  (
  'Fame DP-2000 wh & K92 - Set', 559.00, 10, 54,
  'Set: DP-2000 wh & K92 - Set Set contains 1x Fame DP-2000 WH 1x AKG K 92'
  ),
  (
  'Fame DP-88 + Bag - Set', 209.00, 10, 55,
  'Set: DP-88 + Bag - Set Set contains 1x Fame DP-88 88-Note Stage Piano (Black) 1x Fame BAG-88 für DP-88 1x MUSIC STORE MS 300 Dynamic Headphons 32 Ohm, 102 dB, 20Hz-20KHz'
  ),
  (
  'Casio CT-S1 WH & Bag - Set', 239.00, 10, 55,
  'Set: CT-S1 WH & Bag - Set Set contains 1x Casio CT-S1 WH inkl. TB-1A Sustainpedal 1x Casio SC-500 Keyboard Bag für 61 Tasten Casio Geräte'
  ),
  (
  'keymaXX SP-1 Complete-Set', 269.00, 10, 55,
  'Set: SP-1 Complete-Set Set contains 1x keymaXX SP-1 Digital Piano (Black) 1x MUSIC STORE KB-4 EX 1x MUSIC STORE MS 300 Dynamic Headphons 32 Ohm, 102 dB, 20Hz-20KHz'
  ),
  (
  'Yamaha CK61 - Stage-Set', 1038.99, 10, 56,
  'Set: CK61 - Stage-Set Set contains 1x Yamaha CK61 1x MUSIC STORE OEM-AKS-1185 Table-Style Keyboard Stand (Black) 1x MUSIC STORE TB-1 Sustain Pedal (Black) 1x MUSIC STORE VM18L,Volume pedal,switchable'
  ),
  (
  'Roland VR-730 V-Combo - Stage Set', 1349.00, 10, 56,
  'Set: VR-730 V-Combo - Stage Set Set contains 1x Roland VR-730 V-Combo 1x MUSIC STORE OEM-AKS-1185 Table-Style Keyboard Stand (Black) 1x MUSIC STORE TB-1 Sustain Pedal (Black) 1x MUSIC STORE VM18L,Volume pedal,switchable'
  ),
  (
  'Yamaha PSR SX-720 Standard Bundle', 1549.00, 10, 56,
  'Set: PSR SX-720 Standard Bundle Set contains 1x Yamaha PSR-SX720 1x MUSIC STORE KB-4 EX 1x AKG K 92'
  ),
  (
  'Miditech Midistart Music 25 + KCS Bag Set', 78.99, 10, 58,
  'Set: Music 25 + KCS Bag Set Set contains 1x Miditech Midistart Music 25 USB Masterkeyboard 1x MUSIC STORE Tasche KCS-I Maße: 45 x 25 x 9cm'
  ),
  (
  'Studiologic SL-88 Studio Basic Set', 438.99, 10, 57,
  'Set: SL-88 Studio Basic Set Set contains 1x Studiologic SL-88 Studio 88-Note Keyboard Controller (Black) 1x Studiologic SL Magnetic Music Stand'
  ),
  (
  'Studiologic SL-88 Studio + Bag Set', 499.00, 10, 57,
  'Set: SL-88 Studio + Bag Set Set contains 1x Studiologic SL-88 Studio 88-Note Keyboard Controller (Black) 1x Studiologic Soft-Case Size B (Numa Concert/SL88)'
  ),
  (
  'Wersi Robert Bartha - Live in Concert CD', 12.20, 10, 58,
  'Features: Manufacturer: Wersi'
  ),
  (
  'Ferrofish B4000+ Organ Expander', 389.00, 10, 58,
  'The Ferrofish B4000+ Organ Expander is a quality Organ sound expander. How good could an expander be with the new up to date technical possibilities? The most successful Organ expander in company history answers this very question. With a TFT Display, new processor, newest converter generation and a further improved sound! The draw bars for direct access to the register are naturally still available as well as the ultra robust housing.'
  ),
  (
  'Crumar Mojo 61B Lower Manual', 549.00, 10, 58,
  'The Crumar Mojo61B Additional Manual Keyboard for the Mojo61 is an optional lower manual keyboard that does not require an additional power supply or special tools to make the two keyboards stay together. The Mojo61 simply sits on top of the lower manual, aligned thanks to the rubber feet matching with holes on the bottom keyboard. A single special MIDI cable will connect both units for power and data. This optional lower manual keyboard can accomodate the optional Half-Moon Rotary Switch that is easily attached/detached by two hand screws and a TRS jack cable. You can use the "high trigger" point on the lower manual as well as on the upper manual.'
  ),
  (
  'Roland FR-1X Piano-Type V-Accordion Black', 1699.00, 10, 59,
  'V-Accordions have revolutionized the music world, and Roland has dedicated itself to the ongoing development of these amazing instruments. The FR-1x series is the newest addition to the famous V-Accordion family. These compact, lightweight models offer the same flexibility and portability as their popular FR-1 predecessors, but add significant features, including USB functionality and advanced bellows-pressure circuitry for enhanced'
  ),
  (
  'Roland FR-1XB Button-Type V-Accordion Black', 1849.00, 10, 59,
  'The Roland FR-1xB Black is a compact and lightweight Button type V-Accordion with advanced features. The compact, lightweight Roland FR-1xB offers flexibility and portability with added USB functionality and advanced bellows-pressure circuitry for enhanced sensitivity and precision. With new-generation speakers onboard and an expanded 7-segment/3-character display, the FR-1x sets a powerful standard for a small lightweight accordion.'
  ),
  (
  'Artiphon Orba', 95.00, 10, 60,
  'The Artiphon Orba is a combination of Mini-Synthesizer, Looper and MIDI Controller. This handy Device is in minimalist design and has 8x Sensitive Touch-Pads on the smooth surface that process Velocity, Vibrato and Pressure. Motion and Tilt Sensors inside evaluate various actions such as Shaking, Rubbing or Panning and transmit this to the internal sound generation or as a Controller value to a DAW. A Vibration Motor gives direct feedback about different situations; comparable to a Shock Controller used with Game Consoles.'
  ),
  (
  'Teenage Engineering PO-35 Speak', 99.00, 10, 60,
  'Teenage Engineering PO-35 Speak is a vocal synthesizer combined with a sampler. Via the built-in microphone, voices can be recorded and their character extensively bent via eight integrated voice characters and then played or controlled by the internal 16-step sequencer, followed by interchangeable drum sounds and eight effects on the master.'
  ),
  (
  'Bastl Instruments Kastle Synth V1.5', 125.00, 10, 60,
  'The Bastl Instruments Kastle Synth V1 .5 is a modular Lo-Fi Synth in pocket size. As such, it is ideally suited for the entry into modular synthesis , but also extends the advanced modular system with unique functions. In two Attiny 85 chips it combines a complex oscillator, LFO and waveform generator . Unlike its predecessor, Kastl Synth version 1.5 has a USB port for power supply , a metal case , and a modified sound generation system that is still battery powered, so you can create digital lo-fi sounds, melodic and noisy, soft, hard and drony sounds on the go. The Mini Modular Synthesizer develops its full potential in connection with various modular devices.'
  ),
  (
  'cre8audio Nifty Bundle', 265.00, 10, 61,
  'The Nifty Bundle from Cre8audio is a compact Eurorack system that provides the user with plenty of space for his own equipment. Equipped with a high-performance power supply, a total of 84 HP space for modules, the duophonic MIDI interface and the audio summerizer, the case itself is a great addition to existing setups or the all-round carefree solution for beginners.'
  ),
  (
  'Bastl Instruments Kastle Synth V1.5', 125.00, 10, 61,
  'The Bastl Instruments Kastle Synth V1 .5 is a modular Lo-Fi Synth in pocket size. As such, it is ideally suited for the entry into modular synthesis , but also extends the advanced modular system with unique functions. In two Attiny 85 chips it combines a complex oscillator, LFO and waveform generator . Unlike its predecessor, Kastl Synth version 1.5 has a USB port for power supply , a metal case , and a modified sound generation system that is still battery powered, so you can create digital lo-fi sounds, melodic and noisy, soft, hard and drony sounds on the go. The Mini Modular Synthesizer develops its full potential in connection with various modular devices.'
  ),
  (
  'Bastl Instruments Kastle Synth V1.5', 125.00, 10, 61,
  'The Bastl Instruments Kastle Synth V1 .5 is a modular Lo-Fi Synth in pocket size. As such, it is ideally suited for the entry into modular synthesis , but also extends the advanced modular system with unique functions. In two Attiny 85 chips it combines a complex oscillator, LFO and waveform generator . Unlike its predecessor, Kastl Synth version 1.5 has a USB port for power supply , a metal case , and a modified sound generation system that is still battery powered, so you can create digital lo-fi sounds, melodic and noisy, soft, hard and drony sounds on the go. The Mini Modular Synthesizer develops its full potential in connection with various modular devices.'
  ),
  (
  'MUSIC STORE E-Gitarren Zubehör Paket', 29.00, 10, 74,
  'The Kawai K-15 e PE piano with an impeccable finish and a full, voluminous sound. This is largely due to the K-15"s solid soundboard, which is unique in this price range. This is made of spruce wood, which has been carefully selected and tested. By using very hard tuning pins made of maple, nickel-plated tuning pegs and stable detents made of plywood, the models of the K-series are especially tuning stable and allow the pianist decades of playing pleasure. The Kawai K-15 e PE piano also features the extremely precise Millenium action. The hammer heads are pressed from 100% premium wool.'
  )
;

INSERT INTO users (username, hash, salt) VALUES ('ikienkinzero', 'bc85bbcceedd99b2b7713b2976046b099fb3649d029633803bdfc71c35603e9a', 'rduAoV20DQsHQtQrD0p3SQ==');
INSERT INTO users (username, hash, salt) VALUES ('nd2204', '4a6381563f67d178ef73a2ac4f4119978d2835dfd8f1ec8f67f78460618d93ae', '9NOV6F3hYrOS0HeanJMuDA==');
INSERT INTO users (username, hash, salt) VALUES ('buidinhhuy', 'e2b903a3eee033d9b05d525ccfd904a78365736c5b6f2da3a25e77e4cfbb12fe', 'kh9/vS/RNlKttgRU1uWDaw==');

