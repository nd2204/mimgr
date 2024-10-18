GRANT ALL PRIVILEGES ON mimgrdb.* TO 'mimgr'@'%';
FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS mimgrdb;

USE mimgrdb;

CREATE TABLE IF NOT EXISTS users (
  id       INT         AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL,
  hash     CHAR(64)    NOT NULL,
  salt     CHAR(16)    NOT NULL
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
  image_url      VARCHAR(255),
  created_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
  updated_at     TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY(category_id) REFERENCES categories(category_id)
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

-- Add parent categories first
INSERT INTO categories (category_id, category_name) VALUES 
  (1, 'Keyboards'),
  (2, 'Guitars'),
  (3, 'Drums & Percussion'),
  (4, 'Amplifiers & Effects'),
  (5, 'Wind Instruments'),
  (6, 'String Instruments'),
  (50, 'Accessories');

-- Add subcategory
INSERT INTO categories (category_name, parent_id) VALUES
  ('Pianos', 1), ('Synthesizers', 1),
  ('Electric Guitars', 2), ('Acoustic Guitars', 2), ('Bass Guitars', 2), 
  ('Drum Sets', 3), ('Cymbals', 3), ('Electronic Drums', 3), ('Percussion Instruments', 3),
  ('Guitar Amplifiers', 4), ('Bass Amplifiers', 4), ('Pedals & Effects', 4),
  ('Flutes', 5), ('Clarinets', 5), ('Saxophones', 5), ('Trumpets', 5),
  ('Violins', 6), ('Violas', 6), ('Cellos', 6), ('Double Basses', 6),
  ('Microphones', 50), ('Stands', 50), ('Pop Filters', 50), ('Cables', 50);

INSERT INTO products (name, price, description, stock_quantity, category_id, image_url) VALUES
  (
    'Hanika Natural Torres',
    2799.00,
    'The Hanika Natural Torres Classical Guitar is a professional line 6-string model featuring a spruce top, a rosewood back and sides, an African blackwood fingerboard, and a natural finish. The Natural Torres delivers superior dynamics with a warm and substance-rich sound and crispness. The materials used for building this classical guitar combine to create this beautiful sound with plenty of responsiveness. The back and sides are crafted from select east Indian rosewood and a soundboard made from the finest spruce. The cedro/blackwood neck has been reinforced at its core with hardwood and mahogany. The pierced rosewood-maple-Macassar head and Alessi Standard tuning machines underline the high standards that Hanika players demand.',
    54,
    2,
    ''
  ),
  (
    'The J&D C-200 BK',
    99.00,
    'The J&D C-200 BK Classical Guitar is a 6-string, spruce top, linde sides and bottom, with a mahogany neck, a rosewood fingerboard and bridge, and with a black finish. This is perfect cheap guitar for beginners and students of the classical guitar. Guaranteed to aid the flow of creative juices and musical passion as the perfect stepping stone.',
    5,
    2,
    ''
  ),
  (
    'Fame Bonita Cedro 3/4',
    149.00,
    'The Fame Bonita Cedro 3/4 is the optimal basis for successful guitar learning because, in addition to very obliging playability, the 3/4 concert guitar convinces with a powerful and warm sound, which already unfolds with light strokes. Furthermore, the adjusted saddle and the finely notched bone nut ensure a clean intonation in all 18 frets, while the tuners allow precise tuning of the 6 nylon strings. Visually, the instrument is rounded off by wood bindings, an elaborate soundhole rosette and the matte varnish, which gently emphasizes the even grain of the tonewoods.',
    6,
    2,
    ''
  ),
  (
    'Cordoba F7 Flamenco',
    99.00,
    'The Cordoba F7 Flamenco convinces with its mid-rich and bright sound, which is produced very quickly and falls off just as quickly. In addition to the flat body of typical tonewoods, the fan bracing is largely responsible for the almost attack. The flat neck with wide fretboard gives the player plenty of room to comfortably master even extensive fingering patterns. In addition, the saddle and nut made of bone promise optimal intonation over the entire scale length, while the gold classical tuners allow easy and accurate tuning of the 6 nylon strings. A delicate Binding and an extensive soundhole rosette give the guitar an individual appearance, which is rounded off by the gauzy high-gloss finish in a very tasteful way.',
    67,
    2,
    ''
  ),
  (
    'Yamaha SBP0F56W Stage Custom Birch Classic White',
    1299.00,
    'Features: Manufacturer: Yamaha Shell Material: Birch Shell Surface: Lacquered High-Gloss Colour: Classic White Shell Hardware: Chrome Size tom 1: 10" x 7" Size tom 2: 12" x 8" Size floor tom 1: 14" x 13" Snare size: 14" x 5,5" Size bassdrum: 20" x 17" Air suspension rubber feet: Yes Tuning Safeguard: Yes Rubber Gasket Lined Bass Drum Claws: Yes',
    12,
    3,
    ''
  ),
  (
    'Tama LJK48P-BRM Club Jam Pancake Set Burnt Red Mist',
    415.00,
    'The Tama LJK48P-BRM Club Jam Pancake Set Burnt Red Mist is a four-piece shell set with compact size. The drum set consists of an 18 "x4" Bass drum, a 10 "x3,5" suspended and 13 "x3,5" floor tom in Concert format (without reso head) and a 12 "x4" snare drum as well as a tom holder The shell material of the Club Jam Pancake Set is a hybrid of poplar and Mersawa wood, thus a balanced, warm sound with vintage characteris achieved. The special feature of the Tama Pancake set is, the compact format and the flat shells which allow a crisp sound and easy transport. The whole drum set fits into just one bag if needed, which is available as an option. Suitable is the Tama Club Jam drum kit especially for small clubs, unplugged gigs and the rehearsal room.',
    23,
    3,
    ''
  ),
  (
    'Gretsch Mighty Mini Snare 12"x5,5" Black GTS Mount',
    93.00,
    'The Blackhawk Mighty Mini Snare 12"x5,5" in Black is an inexpensive side snare from Gretsch. The 7-layer poplar wood shell provides a bright yet assertive sound for little money. A GTS Mounting System is already included in the scope of delivery, with which the snare can be quickly and easily attached to almost anywhere on your drum kit. The model has 6 lugs, 1.6 mm thick and triple flanged steel hoops and a 30° bearing edge.',
    22,
    3,
    ''
  ),
  (
    'Pearl Export EXX725SBR/C761 Satin Shadow Black',
    949.00,
    'The Pearl Export EXX725SBR/C761 Satin Shadow Black features 6-ply shells made of a poplar/mahogany mix, these convince with 45° bearing edge, 1.6 mm hoops, Opti-Loc tom suspension and are manufactured with Superior Shell Technology. A complete hardware set and a Sabian sbr cymbal set is included. The drums of the Pearl Export Series come as complete sets and are aimed at the demanding beginner.',
    15,
    3,
    ''
  ),
  (
    'Epiphone Thunderbird 64 Purple Sparkle',
    999.00,
    'The Epiphone Thunderbird 64 Purple Sparkle electric bass from the Inspired by Gibson collection offers all the ingredients of a classic Thunderbird bass and impresses with resonant sounds, an iconic look and proven playability. The classic design by automotive designer Ray Dietrich, who has already been responsible for the bodies of Chrysler, Rolls-Royce and many more, follows the Gibson Firebird, also designed by Ray, and is characterized by a striking offset silhouette. Two in-house humbuckers provide the coveted low-end punch, while solid add-on parts from Graph Tech and Epiphone make up the hardware equipment of the Thunderbird 64. Last but not least, a matching premium Gigbag is included in the scope of delivery.',
    55,
    50,
    ''
  ),
  (
    'MUSIC STORE NP-6 Music Stand',
    16.90,
    'The Music Store Music Stand is a collapsible music stand in black and designed to aid musicians during recital, practice and performance. The stand is height adjustable and comes with a carry bag.',
    50,
    50,
    ''
  ),
  (
    'Fame EMT-985 Metronom/Tuner',
    9.90,
    'With the Fame EMT-985 metronome/tuner, Fame offers a comprehensive all-in-one solution for guitarists, bassists and violinists. The compact device is home to a precise tuner with different tuning modes for guitar, bass, violin, ukulele and even a chromatic tuning mode for all conceivable instruments, while the metronome section provides the right beat for every rhythm. In addition to various beat counting times, rhythmic subdivisions in quarter notes, eighth notes, eighth triplet notes and sixteenth note patterns are available for precise timings and even offers the option of outputting the click via headphones, allowing the Fame EMT-985 to be used as a click track on stage. In addition, the tuners section has an input in 6.35mm jack format to either directly connect instrument cables or directly pick up the resonance of the instrument via the included clip microphone for exact tuning results.',
    50,
    50,
    ''
  ),
  (
    'MUSIC STORE Table Stand MkII',
    18.90,
    'The MUSIC STORE table stand MkII is a stable iron construction that is ideal for all seated applications. For podcasts, making music or holding small cameras, it can be placed perfectly on a surface and brings the devices into the individually desired position. This is also ensured by the easily extendable tube combination, which can be used to adjust the height from 240 mm to 370 mm. The sturdy round base made of cast iron ensures stability and gives the stand an elegant appearance. Six rubber pads suppress vibrations and protect the surface. With a weight of 1.45 kg, it is moreover solid and not easy to knock over during hectic activity.',
    50,
    50,
    ''
  ),
  (
    'MUSIC STORE patch cable 6-pack 0,15 m',
    4.40,
    'The MUSIC STORE patch cable in 0.15 m length belongs to the "Basic Standard" category, which offers a range of rock-solid cables for various applications. These perform exactly what is required at the studio or at home. Since a larger stock of patch cables always has advantages, the scope of delivery includes six pieces in different colors so that you dont lose track even when using several cables.',
    50,
    50,
    ''
  ),
  (
    'Fame EV-1801 Electric Violin Black',
    199.00,
    'The Fame EV-1801 Electric Violin Black offers aspiring and advanced violinists the perfect introduction to the world of electrically amplified violin playing, combining classic string sounds with the creative possibilities of modern effect devices. The instrument in the elegant Reverse-S design features the same playing characteristics of classical violins in 4/4 size, making it easy to implement traditional playing techniques. furthermore, unlike the classic resonator body, the solid body is absolutely insensitive to feedback, allowing the instrument to easily hold its own even at high-volume settings. The pickup system integrated into the back of the body provides suitable connectivity with amplifiers, effects and also headphones for silent practice and provides an additional three-band tone control for adjusting the basses, mids and trebles. furthermore, a matching case, an instrument cable and a well-playable bow made of pernambuco wood are included with the Fame EV-1801 Electric Violin Black.',
    50,
    6,
    ''
  ),
  (
    'Fame UB-2 Electric Upright Bass Carbon',
    649.00,
    'The Fame UB-2 Electric Upright Bass Carbon is a particularly transportable solution for bassists on the way to rehearsal, Studio and performance. The smart upright bass in handy 3/4 size can be played comfortably thanks to the screwable bracket, without neglecting the classic feel of the double bass. A professional piezo pickup system is available for the electrically amplified sound, which features a control panel located on the side of the frame with controls for volume and an additional tone control, and can also be used without any problems via the supplied headphones. furthermore, a practical gig bag, a connection cable and a matching bow are included in the scope of delivery of the Fame UB-2 Electric Upright Bass.',
    50,
    6,
    ''
  ),
  (
    'Fame Handmade Series Violine Maestro 4/4',
    399.00,
    'The Fame Handmade Series Violin Maestro 4/4 is a fully-fledged violin in 4/4 size that is aimed at advanced players with high technical demands. The handmade instrument consists of a solid body made of curly maple with a strong flame and a solid spruce top for a warm and full sound, while the fingerboard, tailpiece, chinrest and tuning pegs are made of genuine ebony. The instrument also features a factory-finished maple bridge and comes with a matching case and a Brazilwood bow with natural hair cover.',
    50,
    6,
    ''
  ),
  (
    'Gewa Cellogarnitur Allegro 3/4',
    899.40,
    'Features: Manufacturer: Gewa Size: 3/4 Top: Spruce Back: Maple Side: Maple Neck: Maple Fretboard: Ebony Bridge: Maple Tuning Peg: Ebony, Swiss model Bow: Brazilwood',
    50,
    6,
    ''
  ),
  (
    'Fame DPP-61 Compact Portable Digital Piano',
    99.40,
    'The DPP-61 from Fame is a lightweight stage piano in a valuable metal housing, which is designed for playing fun and mobility. With a net weight of only 3.4kg including the built-in battery, it allows a playing time of up to four hours regardless of location. The lightweight 61-key keyboard is velocity sensitive and easy to grasp, especially for beginners. With the 32-note polyphony, you have enough reserves so that no note breaks off when playing smoothly. The USB port of the DPP-61 allows you to charge the battery and use it as a USB master keyboard on a computer with music software such as a DAW.',
    50,
    1,
    ''
  ),
  (
    'Yamaha MODX6+',
    1279.40,
    'The Yamaha MODX6+ is a compact and portable 61-key music synthesizer designed specifically for keyboardists on stage and in the studio who want to realize multi-layered and dynamic sounds. Here, the MODX6+ relies on the same powerful technologies of its big brother MONTAGE, which it made famous as Motion Control Synthesis. Two synthesis engines are available here for sound generation: The sample-based AWM2 synthesis engine (Advanced Wave Memory 2) and the FM-X synthesis engine (Frequency Modulation), which can be used at will and also in parallel. This allows the MODX6+ to achieve exceptional sound quality and makes complex sound design possible, which is further enhanced by additional functions. For example, Smart Morph allows morphing between FM-X sounds, bringing forth a variety of new and interactive sounds. Motion Control furthermore allows simultaneous control of up to 128 parameters - automated or manually with the Super Knob.',
    50,
    1,
    ''
  )
  
  

