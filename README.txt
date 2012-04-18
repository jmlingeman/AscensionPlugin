3D Visualization OpenSHAPA plugin for data from the Ascension 3D motion capture system.

To build:
Open project in Netbeans. Do a Clean & Build the first time. Subsequent times should just require a build.

To run:
Copy the contents of the dist/ folder to ~/Library/Application Support/OpenSHAPA/plugins
(this includes both the AscensionPlugin.jar and the lib directory):

cp -rf dist/* ~/Library/Application\ Support/OpenSHAPA/plugins

Now, in order to run the plugin with OpenGL, we need to copy the Processing OpenGL native libs to a directory that Java can see. In the future this will be integrated into the combined .jar file, but this has not been done yet as the plugin is still in its very early states.

To do, copy the files from the libjogl folder to /Library/Java/Extensions.

sudo cp -rf libjogl/* /Library/Java/Extensions/

This is far from ideal, but it will work for now.
