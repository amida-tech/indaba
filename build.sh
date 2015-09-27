cd source/common
ant dist -q
cd ../..

cd source/builder
ant dist -q
cd ../..

cd source/publisher_aggregation
ant dist -q
cd ../..

cd source/control_panel
ant dist -q
cd ../..

cp source/builder/dist/indaba-builder.war dist/FM31.war
cp source/control_panel/dist/ControlPanel.war dist/ControlPanel.war
cp source/publisher_aggregation/dist/Aggregation.war dist/Publisher.war
