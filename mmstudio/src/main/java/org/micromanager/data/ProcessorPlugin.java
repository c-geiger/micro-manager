///////////////////////////////////////////////////////////////////////////////
//PROJECT:       Micro-Manager
//SUBSYSTEM:     Data API
//-----------------------------------------------------------------------------
//
// AUTHOR:       Chris Weisiger, 2015
//
// COPYRIGHT:    University of California, San Francisco, 2015
//
// LICENSE:      This file is distributed under the BSD license.
//               License text is included with the source distribution.
//
//               This file is distributed in the hope that it will be useful,
//               but WITHOUT ANY WARRANTY; without even the implied warranty
//               of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
//
//               IN NO EVENT SHALL THE COPYRIGHT OWNER OR
//               CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
//               INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES.

package org.micromanager.data;

import org.micromanager.MMPlugin;
import org.micromanager.PropertyMap;

/**
 * A ProcessorPlugin is the "root" class for creating Processors, which
 * are used to modify images as they are collected by Micro-Manager. This base
 * class provides access to the various components that allow for creation and
 * configuration of Processors.
 */
public interface ProcessorPlugin extends MMPlugin {
   /**
    * Generate any GUI needed to configure the plugin, using the provided settings to
    * initialize the GUI. Note that the provided settings may be empty but will never
    * be null.
    * @param settings Default values to use to set up the configurator.
    * @return a PluginConfigurator object used to set parameters for the
    *         plugin. This should be a new object each time this method is
    *         called.
    */
   public ProcessorConfigurator createConfigurator(PropertyMap settings);

   /**
    * Generate a ProcessorFactory that can be used to generate Processor
    * instances. The ProcessorFactory should not refer to any external objects;
    * everything it needs to know should be contained within the passed-in
    * PropertyMap.
    * @param settings The settings used to configure the factory. These
    *        settings may have been generated by a PluginConfigurator.
    * @return a ProcessorFactory based on the provided settings.
    */
   public ProcessorFactory createFactory(PropertyMap settings);
}
