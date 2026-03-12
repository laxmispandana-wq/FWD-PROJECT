import java.util.*;

/**
 * ╔══════════════════════════════════════════════════════════════════╗
 * ║          TrackGuard — Linear Data Structures Demo               ║
 * ║          Covers CO1 → CO5 using real project scenarios          ║
 * ╚══════════════════════════════════════════════════════════════════╝
 *
 * How to run in VS Code:
 *   1. Install "Extension Pack for Java" from VS Code Extensions
 *   2. Open this file → Press  F5  or click  ▶ Run
 *
 * CO1 — Sorting & Searching (Bubble, Selection, Binary Search)
 * CO2 — Linked Lists (Singly + Doubly) — Device Registry & Route History
 * CO3 — Stack, Queue, Circular Queue, Priority Queue — Alerts & Undo
 * CO4 — HashMap / HashTable — Fast Device Lookup
 * CO5 — Integrated TrackGuard Application using all above
 */

// ─────────────────────────────────────────────────────────────────────────────
//  HELPER: Device record used across all demos
// ─────────────────────────────────────────────────────────────────────────────
class Device {
    String id;
    String name;
    String status;   // "SAFE" | "LOST" | "MOVING"
    double battery;  // %
    int priority;    // 1 = highest emergency

    Device(String id, String name, String status, double battery, int priority) {
        this.id       = id;
        this.name     = name;
        this.status   = status;
        this.battery  = battery;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-18s | %-7s | Battery: %5.1f%% | Priority: %d",
                id, name, status, battery, priority);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CO2 — SINGLY LINKED LIST  →  Device Registry
// ─────────────────────────────────────────────────────────────────────────────
class SLLNode {
    Device data;
    SLLNode next;
    SLLNode(Device d) { data = d; }
}

class DeviceRegistry {
    private SLLNode head;
    private int size;

    /** Insert at end */
    public void addDevice(Device d) {
        SLLNode newNode = new SLLNode(d);
        if (head == null) { head = newNode; }
        else {
            SLLNode cur = head;
            while (cur.next != null) cur = cur.next;
            cur.next = newNode;
        }
        size++;
    }

    /** Delete by device ID */
    public boolean removeDevice(String id) {
        if (head == null) return false;
        if (head.data.id.equals(id)) { head = head.next; size--; return true; }
        SLLNode cur = head;
        while (cur.next != null && !cur.next.data.id.equals(id))
            cur = cur.next;
        if (cur.next == null) return false;
        cur.next = cur.next.next;
        size--;
        return true;
    }

    /** Search by ID */
    public Device search(String id) {
        SLLNode cur = head;
        while (cur != null) {
            if (cur.data.id.equals(id)) return cur.data;
            cur = cur.next;
        }
        return null;
    }

    /** Print all devices */
    public void display(String label) {
        System.out.println("\n  📋 " + label);
        SLLNode cur = head;
        int i = 1;
        while (cur != null) {
            System.out.println("     " + i++ + ". " + cur.data);
            cur = cur.next;
        }
        System.out.println("     Total registered: " + size);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CO2 — DOUBLY LINKED LIST  →  GPS Route History (bidirectional traverse)
// ─────────────────────────────────────────────────────────────────────────────
class DLLNode {
    String coords;
    DLLNode prev, next;
    DLLNode(String c) { coords = c; }
}

class RouteHistory {
    private DLLNode head, tail;
    private int size;

    public void addCoord(String coords) {
        DLLNode node = new DLLNode(coords);
        if (head == null) { head = tail = node; }
        else { tail.next = node; node.prev = tail; tail = node; }
        size++;
    }

    public void forwardTraverse() {
        System.out.print("     Forward  → ");
        DLLNode cur = head;
        while (cur != null) {
            System.out.print(cur.coords);
            if (cur.next != null) System.out.print(" → ");
            cur = cur.next;
        }
        System.out.println("  (total stops: " + size + ")");
    }

    public void backwardTraverse() {
        System.out.print("     Backward ← ");
        DLLNode cur = tail;
        while (cur != null) {
            System.out.print(cur.coords);
            if (cur.prev != null) System.out.print(" ← ");
            cur = cur.prev;
        }
        System.out.println();
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CO3 — STACK  →  Undo/Redo for device location updates
// ─────────────────────────────────────────────────────────────────────────────
class LocationStack {
    private final LinkedList<String> stack = new LinkedList<>();

    public void push(String location) { stack.push(location); }

    public String pop() {
        if (stack.isEmpty()) return null;
        return stack.pop();
    }

    public String peek() {
        return stack.isEmpty() ? null : stack.peek();
    }

    public int size() { return stack.size(); }

    public void display(String label) {
        System.out.println("     " + label + ": " + stack);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CO3 — QUEUE  →  Alert FIFO Processing
// ─────────────────────────────────────────────────────────────────────────────
class AlertQueue {
    private final LinkedList<String> queue = new LinkedList<>();

    public void enqueue(String alert) { queue.addLast(alert); }

    public String dequeue() {
        return queue.isEmpty() ? null : queue.removeFirst();
    }

    public boolean isEmpty() { return queue.isEmpty(); }

    public void display() {
        System.out.println("     Queue (front → rear): " + queue);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CO3 — CIRCULAR QUEUE  →  Fixed-size GPS coordinate ring buffer
// ─────────────────────────────────────────────────────────────────────────────
class CircularQueue {
    private final String[] arr;
    private int front, rear, count, capacity;

    CircularQueue(int cap) {
        capacity = cap;
        arr = new String[cap];
        front = 0; rear = -1; count = 0;
    }

    public boolean enqueue(String val) {
        if (count == capacity) return false;   // full
        rear = (rear + 1) % capacity;
        arr[rear] = val;
        count++;
        return true;
    }

    public String dequeue() {
        if (count == 0) return null;           // empty
        String val = arr[front];
        front = (front + 1) % capacity;
        count--;
        return val;
    }

    public void display(String label) {
        System.out.print("     " + label + " [cap=" + capacity + "]: ");
        for (int i = 0; i < count; i++)
            System.out.print(arr[(front + i) % capacity] + " ");
        System.out.println("(size=" + count + ")");
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CO3 — PRIORITY QUEUE (MIN-HEAP)  →  Urgent alert dispatching
// ─────────────────────────────────────────────────────────────────────────────
class AlertPQ {
    // Uses Java's built-in PriorityQueue (min-heap by priority field)
    private final PriorityQueue<Device> pq =
            new PriorityQueue<>(Comparator.comparingInt(d -> d.priority));

    public void insert(Device d) { pq.offer(d); }

    public Device pollHighest() { return pq.poll(); }

    public boolean isEmpty() { return pq.isEmpty(); }

    public int size() { return pq.size(); }
}

// ─────────────────────────────────────────────────────────────────────────────
//  CO4 — HASHMAP  →  O(1) device lookup by ID
// ─────────────────────────────────────────────────────────────────────────────
class DeviceHashMap {
    private final HashMap<String, Device> map = new HashMap<>();

    public void put(Device d)               { map.put(d.id, d); }
    public Device get(String id)            { return map.get(id); }
    public boolean contains(String id)      { return map.containsKey(id); }
    public void remove(String id)           { map.remove(id); }
    public int size()                       { return map.size(); }

    public void display(String label) {
        System.out.println("     " + label + " (" + map.size() + " entries):");
        map.values().forEach(d -> System.out.println("       key=\"" + d.id + "\" → " + d));
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  MAIN  →  CO1 through CO5 all run here
// ─────────────────────────────────────────────────────────────────────────────
public class TrackGuardDSA {

    // ── Utility ──────────────────────────────────────────────────────────────
    static void header(String co, String title) {
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.printf( "║  %-52s║%n", co + "  —  " + title);
        System.out.println("╚══════════════════════════════════════════════════════╝");
    }

    static void section(String s) {
        System.out.println("\n  ── " + s + " " + "─".repeat(Math.max(0, 48 - s.length())));
    }

    // ── CO1: Sorting & Searching ──────────────────────────────────────────────
    static void co1Demo() {
        header("CO1", "Sorting & Searching — Device Battery Levels");

        double[] batteries = { 72.5, 18.0, 95.3, 45.1, 8.7, 63.2, 31.9 };
        String[] names     = { "Laptop","Phone","Camera","Backpack","Watch","Tablet","Keys" };

        section("Bubble Sort — Sort by battery ascending");
        // Bubble sort on parallel arrays
        for (int i = 0; i < batteries.length - 1; i++) {
            for (int j = 0; j < batteries.length - 1 - i; j++) {
                if (batteries[j] > batteries[j + 1]) {
                    double tmp = batteries[j]; batteries[j] = batteries[j+1]; batteries[j+1] = tmp;
                    String  tn = names[j];     names[j]     = names[j+1];     names[j+1]     = tn;
                }
            }
        }
        System.out.println("     After Bubble Sort (ascending battery %):");
        for (int i = 0; i < batteries.length; i++)
            System.out.printf("       %d. %-12s → %5.1f%%%n", i+1, names[i], batteries[i]);

        section("Selection Sort — Sort device IDs alphabetically");
        String[] ids = { "TG-C3", "TG-A1", "TG-F6", "TG-B2", "TG-E5", "TG-D4" };
        for (int i = 0; i < ids.length - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < ids.length; j++)
                if (ids[j].compareTo(ids[minIdx]) < 0) minIdx = j;
            String tmp = ids[i]; ids[i] = ids[minIdx]; ids[minIdx] = tmp;
        }
        System.out.println("     After Selection Sort (IDs A→Z): " + Arrays.toString(ids));

        section("Binary Search — Find device with battery = 45.1%");
        // batteries[] is already sorted from bubble sort above
        double target = 45.1;
        int lo = 0, hi = batteries.length - 1, found = -1;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            if (Math.abs(batteries[mid] - target) < 0.01) { found = mid; break; }
            else if (batteries[mid] < target) lo = mid + 1;
            else hi = mid - 1;
        }
        if (found != -1)
            System.out.printf("     ✅ Found '%s' at index %d with %.1f%%%n", names[found], found, batteries[found]);
        else
            System.out.println("     ❌ Not found");

        section("Linear Search — Find 'Phone' by name");
        int linearIdx = -1;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals("Phone")) { linearIdx = i; break; }
        }
        System.out.println(linearIdx >= 0
            ? "     ✅ 'Phone' found at index " + linearIdx + " (battery=" + batteries[linearIdx] + "%)"
            : "     ❌ Not found");

        System.out.println("\n  📊 Complexity Summary:");
        System.out.println("     Bubble Sort   → O(n²) time  | O(1) space");
        System.out.println("     Selection Sort→ O(n²) time  | O(1) space");
        System.out.println("     Binary Search → O(log n) time | O(1) space  [requires sorted array]");
        System.out.println("     Linear Search → O(n) time   | O(1) space");
    }

    // ── CO2: Linked Lists ─────────────────────────────────────────────────────
    static void co2Demo() {
        header("CO2", "Linked Lists — Device Registry & Route History");

        section("Singly Linked List — Device Registry");
        DeviceRegistry registry = new DeviceRegistry();
        registry.addDevice(new Device("TG-A1", "MacBook Pro",      "SAFE",   87.0, 3));
        registry.addDevice(new Device("TG-B2", "iPhone 15",        "LOST",   12.5, 1));
        registry.addDevice(new Device("TG-C3", "Sony Camera",      "MOVING", 54.0, 2));
        registry.addDevice(new Device("TG-D4", "JBL Speaker",      "SAFE",   99.0, 4));
        registry.addDevice(new Device("TG-E5", "Smart Watch",      "LOST",    4.2, 1));
        registry.display("All registered devices");

        System.out.println("\n     🔍 Searching for TG-C3...");
        Device found = registry.search("TG-C3");
        System.out.println("     " + (found != null ? "✅ " + found : "❌ Not found"));

        System.out.println("\n     🗑️  Removing TG-D4 (JBL Speaker)...");
        registry.removeDevice("TG-D4");
        registry.display("After removal");

        section("Doubly Linked List — GPS Route History (bidirectional)");
        RouteHistory route = new RouteHistory();
        String[] coords = {
            "(17.38,78.48)", "(17.39,78.49)", "(17.40,78.50)",
            "(17.41,78.51)", "(17.42,78.52)"
        };
        for (String c : coords) route.addCoord(c);
        route.forwardTraverse();
        route.backwardTraverse();
        System.out.println("     ✅ Bidirectional traversal useful for route replay & rewind");

        System.out.println("\n  📊 Complexity Summary:");
        System.out.println("     SLL insert (end) → O(n)    | SLL search → O(n)");
        System.out.println("     DLL traverse     → O(n)    | Both delete → O(n)");
    }

    // ── CO3: Stack, Queue, Circular Queue, Priority Queue ────────────────────
    static void co3Demo() {
        header("CO3", "Stack · Queue · Circular Queue · Priority Queue");

        // ── Stack ──
        section("Stack (LIFO) — Undo location updates");
        LocationStack undoStack = new LocationStack();
        String[] updates = {"(17.38,78.48)", "(17.39,78.49)", "(17.40,78.50)", "(17.41,78.51)"};
        System.out.println("     Pushing location updates onto undo stack...");
        for (String u : updates) {
            undoStack.push(u);
            System.out.println("       PUSH → " + u);
        }
        undoStack.display("Stack contents (top first)");
        System.out.println("     Undoing last 2 updates (POP)...");
        System.out.println("       POP ← " + undoStack.pop() + "  (undone)");
        System.out.println("       POP ← " + undoStack.pop() + "  (undone)");
        System.out.println("     Current location: " + undoStack.peek());
        undoStack.display("Stack after undo");

        // ── Queue ──
        section("Queue (FIFO) — Alert processing pipeline");
        AlertQueue alertQ = new AlertQueue();
        String[] alerts = {
            "🚨 TG-B2 left geofence",
            "🔋 TG-E5 battery < 5%",
            "📶 TG-C3 signal lost",
            "✅ TG-A1 back in zone"
        };
        System.out.println("     Enqueueing alerts...");
        for (String a : alerts) { alertQ.enqueue(a); System.out.println("       ENQUEUE → " + a); }
        alertQ.display();
        System.out.println("     Processing alerts (DEQUEUE)...");
        while (!alertQ.isEmpty())
            System.out.println("       DEQUEUE → " + alertQ.dequeue() + "  ✅ processed");

        // ── Circular Queue ──
        section("Circular Queue — GPS ring buffer (last 4 pings only)");
        CircularQueue cq = new CircularQueue(4);
        String[] pings = { "P1(17.38,78.48)", "P2(17.39,78.49)", "P3(17.40,78.50)", "P4(17.41,78.51)" };
        for (String p : pings) { cq.enqueue(p); System.out.println("       ENQ → " + p); }
        cq.display("Buffer full");
        System.out.println("     Buffer full — DEQ oldest: " + cq.dequeue());
        cq.enqueue("P5(17.42,78.52)");
        System.out.println("     ENQ new ping P5");
        cq.display("After wrap-around");

        // ── Priority Queue ──
        section("Priority Queue (Min-Heap) — Dispatch urgent alerts first");
        AlertPQ pq = new AlertPQ();
        pq.insert(new Device("TG-B2", "iPhone 15",   "LOST",   12.5, 1));  // most urgent
        pq.insert(new Device("TG-C3", "Sony Camera", "MOVING", 54.0, 3));
        pq.insert(new Device("TG-E5", "Smart Watch", "LOST",    4.2, 1));
        pq.insert(new Device("TG-A1", "MacBook Pro", "SAFE",   87.0, 5));
        pq.insert(new Device("TG-D4", "JBL Speaker", "SAFE",   99.0, 4));
        System.out.println("     Dispatching alerts by priority (1=critical, 5=low):");
        int order = 1;
        while (!pq.isEmpty())
            System.out.println("       #" + order++ + " → " + pq.pollHighest());

        System.out.println("\n  📊 Complexity Summary:");
        System.out.println("     Stack push/pop     → O(1)");
        System.out.println("     Queue enq/deq      → O(1)");
        System.out.println("     Circular Queue     → O(1) enq/deq, O(1) space wrap");
        System.out.println("     Priority Queue     → O(log n) insert/poll | O(n) space");
    }

    // ── CO4: HashMap + Collections ────────────────────────────────────────────
    static void co4Demo() {
        header("CO4", "HashMap — O(1) Device Lookup & Updates");

        section("HashMap — Build device lookup table");
        DeviceHashMap dhm = new DeviceHashMap();
        Device[] devs = {
            new Device("TG-A1", "MacBook Pro",  "SAFE",   87.0, 3),
            new Device("TG-B2", "iPhone 15",    "LOST",   12.5, 1),
            new Device("TG-C3", "Sony Camera",  "MOVING", 54.0, 2),
            new Device("TG-D4", "JBL Speaker",  "SAFE",   99.0, 4),
            new Device("TG-E5", "Smart Watch",  "LOST",    4.2, 1),
        };
        for (Device d : devs) dhm.put(d);
        dhm.display("Initial HashMap");

        section("O(1) Lookup — Find TG-B2 instantly");
        Device result = dhm.get("TG-B2");
        System.out.println("     GET(\"TG-B2\") → " + (result != null ? "✅ " + result : "❌ Not found"));

        section("Update device status");
        Device upd = dhm.get("TG-B2");
        if (upd != null) { upd.status = "SAFE"; upd.battery = 78.0; }
        System.out.println("     Updated TG-B2: " + dhm.get("TG-B2"));

        section("Remove device (TG-D4 deregistered)");
        dhm.remove("TG-D4");
        System.out.println("     REMOVE(\"TG-D4\") done. Size now: " + dhm.size());

        section("Java Collections — List, Deque, Map summary");
        // ArrayList for ordered device list
        List<String> deviceList = new ArrayList<>(Arrays.asList("TG-A1","TG-B2","TG-C3"));
        System.out.println("     ArrayList (ordered): " + deviceList);

        // Deque as sliding window signal monitor
        Deque<Integer> signalWindow = new ArrayDeque<>();
        int[] signals = { 85, 72, 90, 45, 60, 88, 33 };
        int windowSize = 3;
        System.out.print("     Deque sliding window (size=" + windowSize + ") max signals: ");
        for (int s : signals) {
            signalWindow.addLast(s);
            if (signalWindow.size() > windowSize) signalWindow.pollFirst();
            System.out.print(Collections.max(signalWindow) + " ");
        }
        System.out.println();

        // LinkedHashMap preserves insertion order
        Map<String, String> statusMap = new LinkedHashMap<>();
        statusMap.put("TG-A1", "SAFE"); statusMap.put("TG-B2", "SAFE");
        statusMap.put("TG-C3", "MOVING"); statusMap.put("TG-E5", "LOST");
        System.out.println("     LinkedHashMap (insertion order preserved): " + statusMap);

        System.out.println("\n  📊 Complexity Summary:");
        System.out.println("     HashMap get/put/remove  → O(1) avg | O(n) worst (collision)");
        System.out.println("     ArrayList add/get       → O(1) amortized | O(n) insert/delete");
        System.out.println("     Deque addLast/pollFirst → O(1)");
        System.out.println("     LinkedHashMap           → O(1) avg | maintains insertion order");
    }

    // ── CO5: Integrated TrackGuard Application ────────────────────────────────
    static void co5Demo() {
        header("CO5", "Integrated TrackGuard — All DSA Structures Together");

        System.out.println("\n  Scenario: 5 devices registered. System monitors them,");
        System.out.println("  processes alerts by priority, and supports undo of location updates.\n");

        // ── Setup (CO4: HashMap as primary store) ──
        DeviceHashMap deviceStore = new DeviceHashMap();
        Device[] fleet = {
            new Device("TG-A1", "MacBook Pro",  "SAFE",   87.0, 3),
            new Device("TG-B2", "iPhone 15",    "LOST",    9.5, 1),
            new Device("TG-C3", "Sony Camera",  "MOVING", 54.0, 2),
            new Device("TG-D4", "JBL Speaker",  "SAFE",   99.0, 5),
            new Device("TG-E5", "Smart Watch",  "LOST",    3.1, 1),
        };
        for (Device d : fleet) deviceStore.put(d);

        // ── CO2: Linked list registry ──
        DeviceRegistry registry = new DeviceRegistry();
        for (Device d : fleet) registry.addDevice(d);
        registry.display("STEP 1 — Device Registry (Singly Linked List)");

        // ── CO1: Sort devices by battery for display ──
        section("STEP 2 — Sort Devices by Battery (Insertion Sort)");
        Device[] sorted = fleet.clone();
        for (int i = 1; i < sorted.length; i++) {
            Device key = sorted[i];
            int j = i - 1;
            while (j >= 0 && sorted[j].battery > key.battery) {
                sorted[j + 1] = sorted[j]; j--;
            }
            sorted[j + 1] = key;
        }
        System.out.println("     Sorted by battery % (ascending):");
        for (Device d : sorted)
            System.out.printf("       %-18s → %5.1f%% — %s%n", d.name, d.battery, d.status);

        // ── CO3: Priority queue — dispatch alerts ──
        section("STEP 3 — Alert Queue: Prioritize LOST/Critical Devices");
        AlertPQ alertPQ = new AlertPQ();
        for (Device d : fleet)
            if (d.status.equals("LOST") || d.battery < 15.0)
                alertPQ.insert(d);
        System.out.println("     Dispatching " + alertPQ.size() + " alerts (most critical first):");
        while (!alertPQ.isEmpty()) {
            Device d = alertPQ.pollHighest();
            System.out.printf("       🚨 ALERT dispatched → %s  (priority=%d, battery=%.1f%%)%n",
                    d.name, d.priority, d.battery);
        }

        // ── CO3: Stack — undo GPS updates for TG-A1 ──
        section("STEP 4 — Undo/Redo Location Updates (Stack)");
        LocationStack undo = new LocationStack();
        String[] locs = {"(17.38,78.48)","(17.39,78.49)","(17.40,78.50)","(17.41,78.51)"};
        for (String l : locs) undo.push(l);
        System.out.println("     MacBook Pro location history pushed to undo stack.");
        System.out.println("     User triggers UNDO twice:");
        System.out.println("       Undone: " + undo.pop());
        System.out.println("       Undone: " + undo.pop());
        System.out.println("     Current location after undo: " + undo.peek());

        // ── CO2: Doubly LL — route replay ──
        section("STEP 5 — Route Replay via Doubly Linked List");
        RouteHistory route = new RouteHistory();
        for (String l : locs) route.addCoord(l);
        route.forwardTraverse();
        route.backwardTraverse();

        // ── CO3: Circular Queue — rolling GPS pings ──
        section("STEP 6 — Ring Buffer: Last 3 GPS Pings (Circular Queue)");
        CircularQueue ring = new CircularQueue(3);
        String[] pings = {"P1(17.38)","P2(17.39)","P3(17.40)","P4(17.41)","P5(17.42)"};
        for (String p : pings) {
            if (!ring.enqueue(p)) {
                ring.dequeue();  // evict oldest
                ring.enqueue(p);
            }
            System.out.println("       After enqueue " + p + ":");
            ring.display("       Buffer");
        }

        // ── CO4: HashMap fast lookup ──
        section("STEP 7 — Fast Lookup: Fetch device TG-C3 in O(1)");
        Device cam = deviceStore.get("TG-C3");
        System.out.println("     HashMap.get(\"TG-C3\") → " + cam);
        cam.status = "SAFE";
        System.out.println("     Status updated to SAFE. New entry: " + deviceStore.get("TG-C3"));

        // ── Final summary ──
        System.out.println("\n╔══════════════════════════════════════════════════════╗");
        System.out.println("║                  SYSTEM STATUS SUMMARY              ║");
        System.out.println("╠══════════════════════════════════════════════════════╣");
        System.out.printf( "║  %-52s║%n", "CO1 Sorting/Searching ✅  Insertion sort + Binary search");
        System.out.printf( "║  %-52s║%n", "CO2 Linked Lists      ✅  Device registry + Route history");
        System.out.printf( "║  %-52s║%n", "CO3 Stack/Queue       ✅  Undo stack + Alert FIFO + Ring buf");
        System.out.printf( "║  %-52s║%n", "CO3 Priority Queue    ✅  Critical alerts dispatched first");
        System.out.printf( "║  %-52s║%n", "CO4 HashMap           ✅  O(1) device lookup & update");
        System.out.printf( "║  %-52s║%n", "CO5 Integration       ✅  All structures in one application");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println("\n  🛰️  TrackGuard DSA Demo complete. All systems online!");
    }

    // ── Entry Point ───────────────────────────────────────────────────────────
    public static void main(String[] args) {
        System.out.println("═".repeat(56));
        System.out.println("  🛰️  TrackGuard — DSA Demonstration  (CO1 → CO5)");
        System.out.println("═".repeat(56));

        co1Demo();
        co2Demo();
        co3Demo();
        co4Demo();
        co5Demo();
    }
}