# BigFlow
An extensive corpus for network flow measurement and analysis of massive network activity

### Measurement Architecture
Extracts over 158 features based solely on raw network packet header traces. (Note that some features may be repeated...)
All features are extracted in real-time based on several papers. 
Only features that can be extracted directly from the network packet header is extracted (flags + fields - options)
#### Features set overview
Nr. of Feat. | Nr. of Orig. Feat. | Paper
:---:  | :---:  | ---
15 | 17 | [*Online and Scalable Unsupervised Network Anomaly Detection Method (2016)*](http://ieeexplore.ieee.org/document/7740019/)
21 | 22 | [*A preliminary performance comparison of five machine learning algorithms for practical IP traffic flow classification (2006)*](http://dl.acm.org/citation.cfm?id=1163596)
60 | 249 | [*Discriminators for use in flow-based classification (2005)*](https://qmro.qmul.ac.uk/xmlui/bitstream/handle/123456789/5050/RR-05-13.pdf?sequence=1)
62 | 44 | [*Towards an Energy-Efficient Anomaly-Based Intrusion Detection Engine for Embedded Systems (2017)*](http://ieeexplore.ieee.org/document/7463065/?arnumber=7463065)

### Classification Architecture
TBD

###### Note that BigFlow input should be generated by [*PCAPNetworkPacketExporter*](https://github.com/viegaseduardo/PcapNetworkPacketExporter) API
