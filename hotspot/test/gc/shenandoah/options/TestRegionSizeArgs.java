/*
 * Copyright (c) 2016 Red Hat, Inc. and/or its affiliates.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 *
 */

/*
 * @test TestRegionSizeArgs
 * @summary Test that Shenandoah region size args are checked
 * @key gc
 * @library /testlibrary
 * @modules java.base/jdk.internal.misc
 *          java.management
 * @run driver TestRegionSizeArgs
 */

import com.oracle.java.testlibrary.*;

public class TestRegionSizeArgs {
    public static void main(String[] args) throws Exception {
        testInvalidRegionSizes();
        testMinRegionSize();
        testMaxRegionSize();
    }

    private static void testInvalidRegionSizes() throws Exception {

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms2m",
                                                                      "-Xmx2g",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldMatch("Initial heap size");
            output.shouldHaveExitValue(1);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms4m",
                                                                      "-Xmx2g",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldHaveExitValue(0);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms8m",
                                                                      "-Xmx2g",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldHaveExitValue(0);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms100m",
                                                                      "-Xmx2g",
                                                                      "-XX:ShenandoahHeapRegionSize=200m",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldMatch("Invalid -XX:ShenandoahHeapRegionSize option");
            output.shouldHaveExitValue(1);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms100m",
                                                                      "-Xmx2g",
                                                                      "-XX:ShenandoahHeapRegionSize=11m",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldMatch("Invalid -XX:ShenandoahHeapRegionSize option");
            output.shouldHaveExitValue(1);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms100m",
                                                                      "-Xmx2g",
                                                                      "-XX:ShenandoahHeapRegionSize=9m",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldHaveExitValue(0);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms100m",
                                                                      "-Xmx2g",
                                                                      "-XX:ShenandoahHeapRegionSize=255K",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldMatch("Invalid -XX:ShenandoahHeapRegionSize option");
            output.shouldHaveExitValue(1);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms100m",
                                                                      "-Xmx2g",
                                                                      "-XX:ShenandoahHeapRegionSize=260K",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldHaveExitValue(0);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms1g",
                                                                      "-Xmx2g",
                                                                      "-XX:+UnlockExperimentalVMOptions",
                                                                      "-XX:ShenandoahHeapRegionSize=32M",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldHaveExitValue(0);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms1g",
                                                                      "-Xmx2g",
                                                                      "-XX:+UnlockExperimentalVMOptions",
                                                                      "-XX:ShenandoahHeapRegionSize=64M",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldMatch("Invalid -XX:ShenandoahHeapRegionSize option");
            output.shouldHaveExitValue(1);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms1g",
                                                                      "-Xmx2g",
                                                                      "-XX:+UnlockExperimentalVMOptions",
                                                                      "-XX:ShenandoahHeapRegionSize=256K",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldHaveExitValue(0);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms1g",
                                                                      "-Xmx2g",
                                                                      "-XX:+UnlockExperimentalVMOptions",
                                                                      "-XX:ShenandoahHeapRegionSize=128K",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldMatch("Invalid -XX:ShenandoahHeapRegionSize option");
            output.shouldHaveExitValue(1);
        }
    }

    private static void testMinRegionSize() throws Exception {

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms100m",
                                                                      "-Xmx2g",
                                                                      "-XX:+UnlockExperimentalVMOptions",
                                                                      "-XX:ShenandoahMinRegionSize=255K",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldMatch("Invalid -XX:ShenandoahMinRegionSize option");
            output.shouldHaveExitValue(1);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms100m",
                                                                      "-Xmx2g",
                                                                      "-XX:+UnlockExperimentalVMOptions",
                                                                      "-XX:ShenandoahMinRegionSize=1M",
                                                                      "-XX:ShenandoahMaxRegionSize=260K",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldMatch("Invalid -XX:ShenandoahMinRegionSize or -XX:ShenandoahMaxRegionSize");
            output.shouldHaveExitValue(1);
        }
        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms100m",
                                                                      "-Xmx2g",
                                                                      "-XX:+UnlockExperimentalVMOptions",
                                                                      "-XX:ShenandoahMinRegionSize=200m",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldMatch("Invalid -XX:ShenandoahMinRegionSize option");
            output.shouldHaveExitValue(1);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms100m",
                                                                      "-Xmx2g",
                                                                      "-XX:+UnlockExperimentalVMOptions",
                                                                      "-XX:ShenandoahMinRegionSize=11m",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldMatch("Invalid -XX:ShenandoahMinRegionSize option");
            output.shouldHaveExitValue(1);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms100m",
                                                                      "-Xmx2g",
                                                                      "-XX:+UnlockExperimentalVMOptions",
                                                                      "-XX:ShenandoahMinRegionSize=9m",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldHaveExitValue(0);
        }

    }

    private static void testMaxRegionSize() throws Exception {

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms100m",
                                                                      "-Xmx2g",
                                                                      "-XX:+UnlockExperimentalVMOptions",
                                                                      "-XX:ShenandoahMaxRegionSize=255K",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldMatch("Invalid -XX:ShenandoahMaxRegionSize option");
            output.shouldHaveExitValue(1);
        }

        {
            ProcessBuilder pb = ProcessTools.createJavaProcessBuilder("-XX:+UseShenandoahGC",
                                                                      "-Xms100m",
                                                                      "-Xmx2g",
                                                                      "-XX:+UnlockExperimentalVMOptions",
                                                                      "-XX:ShenandoahMinRegionSize=1M",
                                                                      "-XX:ShenandoahMaxRegionSize=260K",
                                                                      "-version");
            OutputAnalyzer output = new OutputAnalyzer(pb.start());
            output.shouldMatch("Invalid -XX:ShenandoahMinRegionSize or -XX:ShenandoahMaxRegionSize");
            output.shouldHaveExitValue(1);
        }
    }
}
