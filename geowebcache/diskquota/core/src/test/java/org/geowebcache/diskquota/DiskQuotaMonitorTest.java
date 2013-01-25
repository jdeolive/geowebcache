/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geowebcache.diskquota;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import org.geowebcache.storage.DefaultStorageFinder;

import junit.framework.TestCase;

public class DiskQuotaMonitorTest extends TestCase {

    public void testDisabledEnvVar() throws Exception {
        DefaultStorageFinder storageFinder = createMock(DefaultStorageFinder.class);
        expect(storageFinder.findEnvVar(DiskQuotaMonitor.GWC_DISKQUOTA_DISABLED)).andReturn("true");

        replay(storageFinder);

        DiskQuotaMonitor monitor = new DiskQuotaMonitor(storageFinder, null, null, null, null, null);
        monitor.afterPropertiesSet();

        assertFalse(monitor.isEnabled());
        verify(storageFinder);
    }

    public void testDisabledConfig() throws Exception {
        DiskQuotaConfig config = createMock(DiskQuotaConfig.class);
        expect(config.isEnabled()).andReturn(false).anyTimes();

        ConfigLoader loader = createMock(ConfigLoader.class);
        expect(loader.loadConfig()).andReturn(config);
        
        DefaultStorageFinder storageFinder = createMock(DefaultStorageFinder.class);
        expect(storageFinder.findEnvVar(DiskQuotaMonitor.GWC_DISKQUOTA_DISABLED)).andReturn("false");
        
        replay(config, loader, storageFinder);

        DiskQuotaMonitor monitor = new DiskQuotaMonitor(storageFinder, loader, null, null, null, null);
        monitor.afterPropertiesSet();

        assertFalse(monitor.isEnabled());
        verify(config, loader);

    }
}
