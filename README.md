   api-recognition-facenetMtcnn
   
This is the dl4j implementation of facenet Environment requirements: 1.JDK1.8 or higher 2.maven 3.3.9 or higher

This application build on https://github.com/cfg1234/dl4j-facenet

 Application uses:
Multi-task Cascaded Convolutional Networks (MTCNN) to detect faces on image
Inception ResNet V1 neural network to build face feature vector
Euclidean distance (as default) to calculate similarity between two face feature vectors. There is cosine distance verifier in application.



Compile: 1. Open this project in STS Spring Tool Suite
         2. To train more image just insert it in  imgLibSample foldert in root project folder and the execute "http://localhost:9091/api/treinar" to train it
         3. To Execute recognition just execute "http://localhost:9091/api/processar" and this will return an string with the name
         4. Thank you



Note: 1. The face library and the test image should not be too large (jpg should be limited to less than 100k), otherwise loading the face library will be very slow 2. It is best not to have multiple faces in the detection image
